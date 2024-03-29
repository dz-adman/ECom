package com.ad.ecom.service.impl;

import com.ad.ecom.common.dto.ResponseMessage;
import com.ad.ecom.common.stub.ResponseType;
import com.ad.ecom.core.context.EComUserLoginContext;
import com.ad.ecom.discounts.persistance.DiscountSubscription;
import com.ad.ecom.discounts.repository.DiscountSubscriptionRepository;
import com.ad.ecom.ecomuser.persistance.EComUser;
import com.ad.ecom.ecomuser.repository.EcomUserRepository;
import com.ad.ecom.ecomuser.stubs.Role;
import com.ad.ecom.orders.dto.OrderInfo;
import com.ad.ecom.orders.dto.OrderTrackingInfo;
import com.ad.ecom.orders.persistance.Order;
import com.ad.ecom.orders.persistance.OrderItem;
import com.ad.ecom.orders.repository.OrderRepository;
import com.ad.ecom.orders.stubs.OrderEvent;
import com.ad.ecom.orders.stubs.OrderStatus;
import com.ad.ecom.products.dto.Item;
import com.ad.ecom.products.persistance.Product;
import com.ad.ecom.products.repository.ProductRepository;
import com.ad.ecom.service.OrdersService;
import com.ad.ecom.ssm.OrdersSMInterceptor;
import com.ad.ecom.user.cart.persistance.Cart;
import com.ad.ecom.user.cart.persistance.CartItem;
import com.ad.ecom.user.cart.repository.CartRepository;
import com.ad.ecom.user.profile.persistance.Address;
import com.ad.ecom.user.profile.repository.AddressRepository;
import com.ad.ecom.util.DateConverter;
import com.ad.ecom.util.SSMUtil;
import com.ad.ecom.util.emailEvent.OrderStatusUpdateEmailEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.config.StateMachineFactory;
import org.springframework.stereotype.Component;

import java.sql.Date;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;
import java.util.stream.Collectors;

@Component
public class OrdersServiceImpl implements OrdersService {

    private static final Logger LOGGER = LogManager.getLogger(OrdersServiceImpl.class);

    @Autowired
    private StateMachineFactory<OrderStatus, OrderEvent> factory;
    @Autowired
    private OrdersSMInterceptor smInterceptor;
    @Autowired
    private EComUserLoginContext loginContext;
    @Autowired
    private ApplicationEventPublisher eventPublisher;
    @Autowired
    private ProductRepository productRepo;
    @Autowired
    private OrderRepository ordersRepo;
    @Autowired
    private CartRepository cartRepo;
    @Autowired
    private AddressRepository addressRepo;
    @Autowired
    private EcomUserRepository userRepo;
    @Autowired
    private DiscountSubscriptionRepository discountSubsRepo;

    @Override
    public ResponseEntity<ResponseMessage> initiateOrder() {
        ResponseMessage responseMessage = new ResponseMessage();
        Optional<Cart> cart = cartRepo.findByUserId(loginContext.getUserInfo().getId());
        if(cart.isEmpty() || cart.get().getItems().isEmpty()) {
            responseMessage.addResponse(ResponseType.ERROR, "Cart is Empty");
            return new ResponseEntity<>(responseMessage, HttpStatus.BAD_REQUEST);
        }
        List<CartItem> items = cart.get().getItems();
        List<Product> orderProducts = new ArrayList<>();
        // Check Stock for order items
        for (CartItem item : items) {
            Optional<Product> product = productRepo.findByProductId(item.getItemProductId());
            if (product.isPresent()) {
                if (product.get().getStock() < item.getItemQuantity())
                    responseMessage.addResponse(ResponseType.ERROR, product.get().getProductId() + " order quantity is Out-Of-Stock");
                else
                    orderProducts.add(product.get());
            }
            if (product.isEmpty())
                responseMessage.addResponse(ResponseType.ERROR, "One or more product not found in our inventory!");
        }
        if (responseMessage.getResponses().containsKey(ResponseType.ERROR))
            return new ResponseEntity(responseMessage, HttpStatus.INTERNAL_SERVER_ERROR);
        else {  // INITIATE ORDER
            Order order = this.createNewOrder(cart.get());
            // ssm
            StateMachine<OrderStatus, OrderEvent> sm = SSMUtil.INSTANCE.buildOrderSSM(order.getOrderId(), ordersRepo, factory, smInterceptor);
            SSMUtil.INSTANCE.processEvent(sm, OrderEvent.PROCESS_ORDER, order);

            // send email for status update
            sendOrderStatusUpdateMail(order);

            // update orderStages
            order.addOrderStages(Arrays.asList(OrderStatus.PENDING_PAYMENT));
            ordersRepo.save(order);

            // clear cart
            cart.get().setItems(Collections.emptyList());
            // reset delivery address to default address
            cart.get().setDeliveryAddressId(addressRepo.findByUserIdAndDefaultAddressTrue(loginContext.getUserInfo().getId()).get().getId());
            cartRepo.save(cart.get());

            responseMessage.setResponseData(order.getOrderId());
            responseMessage.addResponse(ResponseType.SUCCESS, "Order Init Success");
            return new ResponseEntity(responseMessage, HttpStatus.OK);
        }
    }

    @Override
    public ResponseEntity<ResponseMessage> orderInfo(long orderId) {
        ResponseMessage responseMessage = new ResponseMessage();
        OrderInfo orderInfo = new OrderInfo();
        Optional<Order> order = ordersRepo.findByUserIdAndOrderId(loginContext.getUserInfo().getId(), orderId);
        if (order.isEmpty()) {
            responseMessage.addResponse(ResponseType.ERROR, "Invalid OrderId");
            return new ResponseEntity(responseMessage, HttpStatus.BAD_REQUEST);
        } else {
            orderInfo.setOrderId(order.get().getOrderId());
            orderInfo.setInitDate(DateConverter.convertToECcmDate(order.get().getInitDate()));
            List<Item> items = order.get().getItems().stream().map(i -> Item.builder()
                                                                            .itemProductId(i.getItemProductId())
                                                                            .itemProductName(i.getItemProductName())
                                                                            .itemQuantity(i.getItemQuantity())
                                                                            .itemUnit(i.getItemUnit()).build()).collect(Collectors.toList());
            orderInfo.setItems(items);
            orderInfo.setSubTotal(order.get().getSubTotal());
            orderInfo.setTotal(order.get().getTotal());
            orderInfo.setDeliveryAddressId(order.get().getDeliveryAddress().getId());
            orderInfo.setCompletionDate(order.get().getCompletionDate() != null ? DateConverter.convertToECcmDate(order.get().getCompletionDate()) : null);
            if (order.get().getRefundable()) orderInfo.setRefundAmount(order.get().getRefundableAmount());
            responseMessage.setResponseData(orderInfo);
            responseMessage.addResponse(ResponseType.SUCCESS, null);
            return new ResponseEntity(responseMessage, HttpStatus.OK);
        }
    }

    @Override
    public ResponseEntity<ResponseMessage> orderPayment(long orderId) {
        ResponseMessage response = new ResponseMessage();
        Optional<Order> order = ordersRepo.findByUserIdAndOrderId(loginContext.getUserInfo().getId(), orderId);
        if (order.isEmpty()) {
            response.addResponse(ResponseType.ERROR, "Invalid OrderId!");
            return new ResponseEntity(response, HttpStatus.BAD_REQUEST);
        }
        StateMachine<OrderStatus, OrderEvent> sm = SSMUtil.INSTANCE.buildOrderSSM(orderId, ordersRepo, factory, smInterceptor);
        try {
            CompletableFuture<Boolean> paymentStatus = initOrderPayment(order.get());
            paymentStatus.get(60, TimeUnit.SECONDS);
            if (!paymentStatus.isDone()) paymentStatus.complete(false);
            boolean status = paymentStatus.get();
            if (status) {
                // ssm
                SSMUtil.INSTANCE.processEvent(sm, OrderEvent.PAYMENT_SUCCESSFUL, order.get());

                // update inventory product-stock
                updateInventoryReduceStock(order.get());

                // update orderStages
                order.get().addOrderStages(Arrays.asList(OrderStatus.PAID));
                // set paid flag
                order.get().setPaid(true);
                ordersRepo.save(order.get());


                // send email for status update
                sendOrderStatusUpdateMail(order.get());

                // set transaction reference number in responseData
                response.setResponseData(orderId);
                response.addResponse(ResponseType.SUCCESS, "Payment Successful");
                return new ResponseEntity(response, HttpStatus.OK);
            } else {
                // ssm
                SSMUtil.INSTANCE.processEvent(sm, OrderEvent.PAYMENT_FAILED, order.get());

                // send email for status update
                sendOrderStatusUpdateMail(order.get());

                // update orderStages
                order.get().addOrderStages(Arrays.asList(OrderStatus.PENDING_PAYMENT));
                ordersRepo.save(order.get());

                // set transaction reference number in responseData
                response.setResponseData(orderId);
                response.addResponse(ResponseType.ERROR, "Payment failed!");
                return new ResponseEntity(response, HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } catch (Exception ex) {
            ex.printStackTrace();

            // ssm
            SSMUtil.INSTANCE.processEvent(sm, OrderEvent.PAYMENT_FAILED, order.get());

            // send email for status update
            sendOrderStatusUpdateMail(order.get());

            // update orderStages
            order.get().addOrderStages(Arrays.asList(OrderStatus.PENDING_PAYMENT));
            ordersRepo.save(order.get());

            response.addResponse(ResponseType.ERROR, "Payment failed!");
            return new ResponseEntity(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity<ResponseMessage> initiateShipment(long orderId) {
        ResponseMessage responseMessage = new ResponseMessage();
        Optional<Order> order = ordersRepo.findById(orderId);
        if (order.isEmpty()) {
            responseMessage.addResponse(ResponseType.ERROR, "Invalid OrderId!");
            return new ResponseEntity(responseMessage, HttpStatus.BAD_REQUEST);
        } else {
            // ssm
            StateMachine<OrderStatus, OrderEvent> sm = SSMUtil.INSTANCE.buildOrderSSM(orderId, ordersRepo, factory, smInterceptor);
            SSMUtil.INSTANCE.processEvent(sm, OrderEvent.INIT_SHIPMENT, order.get());

            // send email for status update
            sendOrderStatusUpdateMail(order.get());

            // update orderStages
            order.get().addOrderStages(Arrays.asList(OrderStatus.SHIPPED));
            ordersRepo.save(order.get());


            responseMessage.setResponseData(orderId);
            responseMessage.addResponse(ResponseType.SUCCESS, "Order Shipment Initiated");
            return new ResponseEntity(responseMessage, HttpStatus.OK);
        }
    }

    @Override
    public ResponseEntity<ResponseMessage> cancelOrder(long orderId) {
        ResponseMessage responseMessage = new ResponseMessage();
        Optional<Order> order = ordersRepo.findByUserIdAndOrderId(loginContext.getUserInfo().getId(), orderId);
        if (order.isEmpty()) {
            responseMessage.addResponse(ResponseType.ERROR, "Invalid OrderId!");
            return new ResponseEntity(responseMessage, HttpStatus.BAD_REQUEST);
        } else {
            // ssm
            StateMachine<OrderStatus, OrderEvent> sm = SSMUtil.INSTANCE.buildOrderSSM(orderId, ordersRepo, factory, smInterceptor);
            SSMUtil.INSTANCE.processEvent(sm, OrderEvent.CANCEL_ORDER, order.get());

            // send email for status update
            sendOrderStatusUpdateMail(order.get());

            // set cancelled flag and cancellation date
            order.get().setCancelled(true);
            order.get().setCancellationDate(new Date(System.currentTimeMillis()));

            // Initiate refund if applicable
            if (order.get().getRefundableAmount() > 0.0) {
                order.get().setRefundable(true);
                return refundOrder(order.get(), sm);
            } else {
                // ssm update - RefundAndComplete
                SSMUtil.INSTANCE.processEvent(sm, OrderEvent.COMPLETE, order.get());

                // send email for status update
                sendOrderStatusUpdateMail(order.get());

                // update orderStages
                order.get().addOrderStages(Arrays.asList(OrderStatus.CANCELLED, OrderStatus.CANCELLED_AND_COMPLETE));
                ordersRepo.save(order.get());

                responseMessage.setResponseData(orderId);
                responseMessage.addResponse(ResponseType.SUCCESS, "Order Cancelled");
                return new ResponseEntity(responseMessage, HttpStatus.OK);
            }
        }
    }

    @Override
    public ResponseEntity<ResponseMessage> trackOrder(long orderId) {
        ResponseMessage responseMessage = new ResponseMessage();
        Optional<Order> order;
        if (loginContext.getUserInfo().getRole() == Role.ROLE_USER)
            order = ordersRepo.findByUserIdAndOrderId(loginContext.getUserInfo().getId(), orderId);
        else order = ordersRepo.findById(orderId);
        if (order.isEmpty()) {
            responseMessage.addResponse(ResponseType.ERROR, "Invalid OrderId!");
            return new ResponseEntity(responseMessage, HttpStatus.BAD_REQUEST);
        } else {
            OrderTrackingInfo orderTrackingInfo = OrderTrackingInfo.builder()
                                                                   .trackingInfo(order.get().getOrderStagesList())
                                                                   .build();
            responseMessage.setResponseData(orderTrackingInfo);
            responseMessage.addResponse(ResponseType.SUCCESS, "Order Tracking Info");
            return new ResponseEntity(responseMessage, HttpStatus.OK);
        }
    }

    @Override
    public ResponseEntity<ResponseMessage> deliverOrder(long orderId) {
        ResponseMessage responseMessage = new ResponseMessage();
        Optional<Order> order = ordersRepo.findById(orderId);
        if (order.isEmpty()) {
            responseMessage.addResponse(ResponseType.ERROR, "Invalid OrderId!");
            return new ResponseEntity(responseMessage, HttpStatus.BAD_REQUEST);
        } else {
            // ssm
            StateMachine<OrderStatus, OrderEvent> sm = SSMUtil.INSTANCE.buildOrderSSM(orderId, ordersRepo, factory, smInterceptor);
            SSMUtil.INSTANCE.processEvent(sm, OrderEvent.DELIVERED, order.get());

            // send email for status update
            sendOrderStatusUpdateMail(order.get());

            // update orderStages
            order.get().addOrderStages(Arrays.asList(OrderStatus.DELIVERED_AND_COMPLETE));
            // set completion date
            order.get().setCompletionDate(new Date(System.currentTimeMillis()));
            ordersRepo.save(order.get());

            responseMessage.setResponseData(orderId);
            responseMessage.addResponse(ResponseType.SUCCESS, "Order Delivered and Complete");
            return new ResponseEntity(responseMessage, HttpStatus.OK);
        }
    }

    @Override
    public ResponseEntity<ResponseMessage> markManualRefund(long orderId) {
        ResponseMessage responseMessage = new ResponseMessage();
        Optional<Order> order = ordersRepo.findById(orderId);
        if (order.isEmpty()) {
            responseMessage.addResponse(ResponseType.ERROR, "Invalid OrderId!");
            return new ResponseEntity(responseMessage, HttpStatus.BAD_REQUEST);
        } else {
            // ssm - refund complete
            StateMachine<OrderStatus, OrderEvent> sm = SSMUtil.INSTANCE.buildOrderSSM(orderId, ordersRepo, factory, smInterceptor);
            SSMUtil.INSTANCE.processEvent(sm, OrderEvent.REFUND_COMPLETE, order.get());

            // send email for status update
            sendOrderStatusUpdateMail(order.get());

            // ssm update - refunded and complete
            SSMUtil.INSTANCE.processEvent(sm, OrderEvent.COMPLETE, order.get());

            // send email for status update
            sendOrderStatusUpdateMail(order.get());

            // update orderStages
            order.get().addOrderStages(Arrays.asList(OrderStatus.REFUNDED, OrderStatus.REFUNDED_AND_COMPLETE));
            // set completion date
            order.get().setCompletionDate(new Date(System.currentTimeMillis()));
            ordersRepo.save(order.get());

            // set transaction reference number in responseData
            responseMessage.setResponseData(orderId);
            responseMessage.addResponse(ResponseType.SUCCESS, "Refunded and Complete");
            return new ResponseEntity(responseMessage, HttpStatus.OK);
        }
    }

    private Order createNewOrder(Cart cart) {
        Order order = new Order();
        order.setUserId(loginContext.getUserInfo().getId());
        List<OrderItem> orderItems = cart.getItems().stream().map(i -> OrderItem.builder()
                                                                                     .itemProductId(i.getItemProductId())
                                                                                     .itemProductName(i.getItemProductName())
                                                                                     .itemQuantity(i.getItemQuantity())
                                                                                     .itemUnit(i.getItemUnit())
                                                                                     .order(order).build()).collect(Collectors.toList());
        order.setItems(orderItems);
        order.setInitDate(new Date(System.currentTimeMillis()));
        order.setStatus(OrderStatus.PENDING_PAYMENT);
        List<OrderStatus> orderStages = new ArrayList<>(Arrays.asList(OrderStatus.INITIATED));
        order.setOrderStages(orderStages);

        if(Optional.ofNullable(cart.getDeliveryAddressId()).isPresent()) {
            Optional<Address> givenAddress = addressRepo.findById(cart.getDeliveryAddressId());
            if(givenAddress.isPresent())
                order.setDeliveryAddress(givenAddress.get());
        }
        if(order.getDeliveryAddress() == null)
            order.setDeliveryAddress(addressRepo.findByUserIdAndDefaultAddressTrue(loginContext.getUserInfo().getId()).get());

        Map<CartItem, Product> itemProductMap = new HashMap<>();
        for (CartItem item : cart.getItems()) {
            Optional<Product> product = productRepo.findByProductId(item.getItemProductId());
            itemProductMap.put(item, product.get());
        }
        order.setSubTotal(itemProductMap.entrySet().stream().mapToDouble(e -> e.getValue().getPrice() * e.getKey().getItemQuantity()).sum());
        List<DiscountSubscription> allSubs = new ArrayList<>();
        double total = 0.0;
        for (Map.Entry<CartItem, Product> e : itemProductMap.entrySet()) {
            List<DiscountSubscription> subscriptions = e.getValue().getDiscountsApplicableOnProduct(discountSubsRepo);
            allSubs.addAll(subscriptions);
            total += (e.getValue().getPrice() - e.getValue().getDiscountOnProduct(discountSubsRepo)) * e.getKey().getItemQuantity();
        }
        order.setDiscountCodes(allSubs.stream().map(subs -> subs.getDiscount().getCode()).collect(Collectors.toList()));
        order.setTotal(total);
        order.calculateAndSetRefundableAmountAndFlag(productRepo, discountSubsRepo);
        order.setPaid(false);
        ordersRepo.save(order);
        return order;
    }

    private CompletableFuture<Boolean> initOrderPayment(final Order order) {
        // Integrate Payment Gateway API and use in the supplier
        Supplier<Boolean> supplier = () -> true;
        return CompletableFuture.supplyAsync(supplier);
    }

    private void updateInventoryReduceStock(Order order) {
        for(OrderItem oi : order.getItems()) {
            String pId = oi.getItemProductId();
            long quantity = oi.getItemQuantity();
            Product product = productRepo.findByProductId(pId).get();
            product.setStock(product.getStock() - quantity);
            productRepo.save(product);
        }
    }

    private ResponseEntity<ResponseMessage> refundOrder(Order order, StateMachine<OrderStatus, OrderEvent> sm) {
        ResponseMessage responseMessage = new ResponseMessage();
        try {
            // ssm update - Init Refund
            SSMUtil.INSTANCE.processEvent(sm, OrderEvent.INIT_REFUND, order);

            // send email for status update
            sendOrderStatusUpdateMail(order);

            CompletableFuture<Boolean> refundStatus = initOrderRefund(order);
            refundStatus.get(60, TimeUnit.SECONDS);
            if (!refundStatus.isDone()) refundStatus.complete(false);
            boolean status = refundStatus.get();
            if (status) {
                // ssm update - Refund Complete
                SSMUtil.INSTANCE.processEvent(sm, OrderEvent.REFUND_COMPLETE, order);

                // send email for status update
                sendOrderStatusUpdateMail(order);

                // ssm update - RefundAndComplete
                SSMUtil.INSTANCE.processEvent(sm, OrderEvent.COMPLETE, order);

                // send email for status update
                sendOrderStatusUpdateMail(order);

                // update orderStages
                order.addOrderStages(Arrays.asList(OrderStatus.REFUNDED, OrderStatus.REFUNDED_AND_COMPLETE));
                // set completion date
                order.setCompletionDate(new Date(System.currentTimeMillis()));
                // set refunded flag
                order.setRefunded(true);
                ordersRepo.save(order);

                // set transaction reference number in responseData
                //response.setResponseData();
                responseMessage.addResponse(ResponseType.SUCCESS, "Refunded and Complete");
                return new ResponseEntity(responseMessage, HttpStatus.INTERNAL_SERVER_ERROR);
            }
            // ssm update - Refund Failed
            SSMUtil.INSTANCE.processEvent(sm, OrderEvent.REFUND_FAILED, order);

            // send email for status update
            sendOrderStatusUpdateMail(order);

            // update orderStages
            order.addOrderStages(Arrays.asList(OrderStatus.REFUND_PENDING));
            ordersRepo.save(order);

            // set transaction reference number in responseData
            responseMessage.setResponseData(order.getOrderId());
            responseMessage.addResponse(ResponseType.ERROR, "Refund failed!");
            return new ResponseEntity(responseMessage, HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (Exception ex) {
            ex.printStackTrace();

            // ssm update - Refund Failed
            SSMUtil.INSTANCE.processEvent(sm, OrderEvent.REFUND_FAILED, order);

            // send email for status update
            sendOrderStatusUpdateMail(order);

            // update orderStages
            order.addOrderStages(Arrays.asList(OrderStatus.REFUND_PENDING));
            ordersRepo.save(order);

            responseMessage.addResponse(ResponseType.ERROR, "Refund failed!");
            return new ResponseEntity(responseMessage, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private CompletableFuture<Boolean> initOrderRefund(final Order order) {
        // Integrate Payment Gateway API and use in the supplier
        Supplier<Boolean> supplier = () -> true;
        return CompletableFuture.supplyAsync(supplier);
    }

    private void sendOrderStatusUpdateMail(Order order) {
        Optional<EComUser> user = userRepo.findById(order.getUserId());
        eventPublisher.publishEvent(new OrderStatusUpdateEmailEvent(this, user.get(), order.getOrderId(), order.getStatus()));
    }
}
