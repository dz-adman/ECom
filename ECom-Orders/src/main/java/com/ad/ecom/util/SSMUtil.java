package com.ad.ecom.util;

import com.ad.ecom.orders.persistance.Order;
import com.ad.ecom.orders.repository.OrderRepository;
import com.ad.ecom.orders.stubs.OrderEvent;
import com.ad.ecom.orders.stubs.OrderStatus;
import com.ad.ecom.ssm.OrdersSMInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.config.StateMachineFactory;
import org.springframework.statemachine.service.DefaultStateMachineService;
import org.springframework.statemachine.support.DefaultStateMachineContext;
import reactor.core.publisher.Mono;

import java.util.Optional;

public enum SSMUtil {
    INSTANCE;
    private static final String ORDER_ID = "orderId";

    @Autowired
    private DefaultStateMachineService<OrderStatus, OrderEvent> ssmService;

    public StateMachine<OrderStatus, OrderEvent> buildOrderSSM(long orderId, OrderRepository ordersRepo,
                                                               StateMachineFactory<OrderStatus, OrderEvent> factory,
                                                               OrdersSMInterceptor smInterceptor) {
        Optional<Order> order = ordersRepo.findById(orderId);
        StateMachine<OrderStatus, OrderEvent> sm = null;
        if(order.isPresent()) {
            String orderIdKey = Long.toString(order.get().getOrderId());
            sm = ssmService.acquireStateMachine(orderIdKey, false);
            sm.stopReactively().block();
            sm.getStateMachineAccessor().doWithAllRegions(sma -> {
                sma.addStateMachineInterceptor(smInterceptor);
                sma.resetStateMachineReactively(new DefaultStateMachineContext<>(order.get().getStatus(), null, null, null)).subscribe();
            });
        }
        sm.startReactively().block();
        return sm;
    }

    public void processEvent(StateMachine<OrderStatus, OrderEvent> sm, OrderEvent event, Order order) {
        Message<OrderEvent> processingMsg = MessageBuilder.withPayload(event)
                                                          .setHeader(ORDER_ID, order.getOrderId())
                                                          .build();
        sm.sendEvent(Mono.just(processingMsg)).subscribe();
    }
}
