package com.ad.ecom.service.impl;

import com.ad.ecom.common.dto.Item;
import com.ad.ecom.common.stub.ResponseMessage;
import com.ad.ecom.common.stub.ResponseType;
import com.ad.ecom.core.context.EComUserLoginContext;
import com.ad.ecom.discounts.persistance.DiscountSubscription;
import com.ad.ecom.discounts.repository.DiscountSubscriptionsRepository;
import com.ad.ecom.orders.dto.OrderInfo;
import com.ad.ecom.orders.persistance.Order;
import com.ad.ecom.orders.repository.OrdersRepository;
import com.ad.ecom.orders.stubs.OrderEvent;
import com.ad.ecom.orders.stubs.OrderStatus;
import com.ad.ecom.products.persistance.Products;
import com.ad.ecom.products.repository.ProductsRepository;
import com.ad.ecom.service.OrdersService;
import com.ad.ecom.ssm.OrdersSMInterceptor;
import com.ad.ecom.user.repository.AddressRepository;
import com.ad.ecom.util.DateConverter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.config.StateMachineFactory;
import org.springframework.statemachine.support.DefaultStateMachineContext;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.sql.Date;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;
import java.util.stream.Collectors;

@Component
public class OrdersServiceImpl implements OrdersService {

    private static final Logger LOGGER = LogManager.getLogger(OrdersServiceImpl.class);
    private static final String ORDER_ID = "orderId";

    @Autowired
    private EComUserLoginContext loginContext;
    @Autowired
    private ProductsRepository productsRepo;
    @Autowired
    private OrdersRepository ordersRepo;
    @Autowired
    private AddressRepository addressRepo;
    @Autowired
    private DiscountSubscriptionsRepository discountSubsRepo;
    @Autowired
    private OrdersSMInterceptor smInterceptor;
    @Autowired
    private StateMachineFactory<OrderStatus, OrderEvent> factory;

    @Override
    public StateMachine<OrderStatus, OrderEvent> build(long orderId) {
        Optional<Order> order = ordersRepo.findById(orderId);
        StateMachine<OrderStatus, OrderEvent> sm = null;
        if(order.isPresent()) {
            String orderIdKey = Long.toString(order.get().getOrderId());
            sm = this.factory.getStateMachine(orderIdKey);
            sm.stopReactively().block();
            sm.getStateMachineAccessor().doWithAllRegions(sma -> {
                sma.addStateMachineInterceptor(smInterceptor);
                sma.resetStateMachineReactively(new DefaultStateMachineContext<>(order.get().getStatus(), null, null, null)).subscribe();
            });
        }
        sm.startReactively().block();
        return sm;
    }

    @Override
    public ResponseEntity<ResponseMessage> initiateOrder(OrderInfo orderInfo) {
        ResponseMessage responseMessage = new ResponseMessage();
        List<Item> items = orderInfo.getItems();
        if(!items.isEmpty()) {
            List<Products> orderProducts = new ArrayList<>();
            for(Item item : items) {
                Optional<Products> product = productsRepo.findByProductId(item.getItemProductId());
                if(product.isPresent()) {
                    if(product.get().getStock() < item.getItemQuantity())
                        responseMessage.addResponse(ResponseType.ERROR, product.get().getProductId() + " order quantity is Out-Of-Stock");
                    else
                        orderProducts.add(product.get());
                }
                if(product.isEmpty())
                    responseMessage.addResponse(ResponseType.ERROR, "One or more product not found in our inventory!");
            }
            if(responseMessage.getResponses().containsKey(ResponseType.ERROR))
                return new ResponseEntity(responseMessage, HttpStatus.INTERNAL_SERVER_ERROR);
            else {  // INITIATE ORDER
                Order order = this.createNewOrder(orderInfo);
                // ssm
                StateMachine<OrderStatus, OrderEvent> sm = this.build(order.getOrderId());
                Message<OrderEvent> processingMsg = MessageBuilder.withPayload(OrderEvent.PROCESS_ORDER)
                                                                  .setHeader(ORDER_ID, order.getOrderId())
                                                                  .build();
                sm.sendEvent(Mono.just(processingMsg)).subscribe();

                responseMessage.addResponse(ResponseType.SUCCESS, String.format("OrderId: %s", order.getOrderId()));
                return new ResponseEntity(responseMessage, HttpStatus.OK);
            }
        }
        return new ResponseEntity("Order can't be Empty", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<ResponseMessage> orderInfo(long orderId) {
        ResponseMessage responseMessage = new ResponseMessage();
        OrderInfo orderInfo = new OrderInfo();
        Optional <Order> order = ordersRepo.findById(orderId);
        if(order.isEmpty()) {
            responseMessage.addResponse(ResponseType.ERROR, "Invalid OrderId");
            return new ResponseEntity(responseMessage, HttpStatus.BAD_REQUEST);
        } else {
            orderInfo.setOrderId(order.get().getOrderId());
            orderInfo.setInitDate(DateConverter.convertToECcmDate(order.get().getInitDate()));
            orderInfo.setItems(order.get().getItems());
            orderInfo.setSubTotal(order.get().getSubTotal());
            orderInfo.setTotal(order.get().getTotal());
            orderInfo.setDeliveryAddress(order.get().getDeliveryAddress());
            orderInfo.setCompletionDate(order.get().getCompletionDate() != null ? DateConverter.convertToECcmDate(order.get().getCompletionDate()) : null);
            // TODO TEST refundAmount in debug mode for null
            orderInfo.setRefundAmount(order.get().getRefundAmount());
            responseMessage.setResponseData(orderInfo);
            responseMessage.addResponse(ResponseType.SUCCESS, null);
            return new ResponseEntity(responseMessage, HttpStatus.OK);
        }
    }

    @Override
    public ResponseEntity<ResponseMessage> orderPayment(long orderId) {
        ResponseMessage response = new ResponseMessage();
        Optional<Order> order = ordersRepo.findById(orderId);
        if(order.isEmpty()) {
            response.addResponse(ResponseType.ERROR, "Invalid OrderId!");
            return new ResponseEntity(response, HttpStatus.BAD_REQUEST);
        }
        try {
            CompletableFuture<Boolean> paymentStatus = initOrderPayment(order.get().getOrderId());
            paymentStatus.get(60, TimeUnit.SECONDS);
            if(!paymentStatus.isDone()) paymentStatus.complete(false);
            boolean status = paymentStatus.get();
            if(status) {
                // ssm
                StateMachine<OrderStatus, OrderEvent> sm = this.build(orderId);
                Message<OrderEvent> processingMsg = MessageBuilder.withPayload(OrderEvent.PAYMENT_SUCCESSFUL)
                                                                  .setHeader(ORDER_ID, orderId)
                                                                  .build();
                sm.sendEvent(Mono.just(processingMsg)).subscribe();

                response.addResponse(ResponseType.SUCCESS, "Payment Successful.");
                return new ResponseEntity(response, HttpStatus.OK);
            }
            else {
                response.addResponse(ResponseType.ERROR, "Payment failed!");
                return new ResponseEntity(response, HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } catch (Exception ex) {}

        response.addResponse(ResponseType.ERROR, "Payment failed!");
        return new ResponseEntity(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<ResponseMessage> initiateShipment(long orderId) {
        ResponseMessage responseMessage = new ResponseMessage();
        Optional<Order> order = ordersRepo.findById(orderId);
        if(order.isEmpty()) {
            responseMessage.addResponse(ResponseType.ERROR, "Invalid OrderId!");
            return new ResponseEntity(responseMessage, HttpStatus.BAD_REQUEST);
        } else {
            // ssm
            StateMachine<OrderStatus, OrderEvent> sm = this.build(orderId);
            Message<OrderEvent> processingMsg = MessageBuilder.withPayload(OrderEvent.INIT_SHIPMENT)
                                                              .setHeader(ORDER_ID, orderId)
                                                              .build();
            sm.sendEvent(Mono.just(processingMsg)).subscribe();

            responseMessage.addResponse(ResponseType.SUCCESS, "Order Shipment Initiated");
            return new ResponseEntity(responseMessage, HttpStatus.OK);
        }
    }

    @Override
    public ResponseEntity<ResponseMessage> cancelOrder() {
        return null;
    }

    @Override
    public ResponseEntity<ResponseMessage> trackOrder() {
        return null;
    }

    private Order createNewOrder(OrderInfo orderInfo) {
        Order order = new Order();
        order.setUserId(loginContext.getUserInfo().getId());
        order.setItems(orderInfo.getItems());
        order.setInitDate(new Date(System.currentTimeMillis()));
        order.setStatus(OrderStatus.PENDING_PAYMENT);
        order.setDeliveryAddress(orderInfo.getDeliveryAddress());
        Map<Item, Products> itemProductMap = new HashMap<>();
        for(Item item : orderInfo.getItems()) {
            Optional<Products> product = productsRepo.findByProductId(item.getItemProductId());
            itemProductMap.put(item, product.get());
        }
        order.setSubTotal(itemProductMap.entrySet().stream().mapToDouble(e -> e.getValue().getPrice()*e.getKey().getItemQuantity()).sum());
        double total = 0.0;
        for(Map.Entry<Item, Products> e : itemProductMap.entrySet()) {
            List<DiscountSubscription> subscriptions = discountSubsRepo.findByProductId(e.getValue().getId());
            order.setDiscountCodes(subscriptions.stream().map(subs -> subs.getDiscount().getCode()).collect(Collectors.toList()));
            total += (e.getValue().getPrice() - e.getValue().getDiscountOnProduct(discountSubsRepo)) * e.getKey().getItemQuantity();
        }
        order.setTotal(total);
        order.setPaid(false);
        ordersRepo.save(order);
        return order;
    }

    private CompletableFuture<Boolean> initOrderPayment(final long orderId) {
        // Integrate Payment Gateway API and use in the supplier
        Supplier<Boolean> supplier = () -> true;
        return CompletableFuture.supplyAsync(supplier);
    }
}
