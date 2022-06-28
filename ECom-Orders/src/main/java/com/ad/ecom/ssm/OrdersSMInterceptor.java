package com.ad.ecom.ssm;

import com.ad.ecom.orders.persistance.Order;
import com.ad.ecom.orders.repository.OrdersRepository;
import com.ad.ecom.orders.stubs.OrderEvent;
import com.ad.ecom.orders.stubs.OrderStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.state.State;
import org.springframework.statemachine.support.StateMachineInterceptorAdapter;
import org.springframework.statemachine.transition.Transition;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class OrdersSMInterceptor extends StateMachineInterceptorAdapter<OrderStatus, OrderEvent> {

    private static final String ORDER_ID = "orderId";

    @Autowired
    private OrdersRepository ordersRepo;

    @Override
    public void preStateChange(State<OrderStatus, OrderEvent> state, Message<OrderEvent> message, Transition<OrderStatus, OrderEvent> transition, StateMachine<OrderStatus, OrderEvent> stateMachine, StateMachine<OrderStatus, OrderEvent> rootStateMachine) {
        Optional.ofNullable(message).ifPresent(msg -> {
            Optional.ofNullable(Long.class.cast(msg.getHeaders().getOrDefault(ORDER_ID, -1L))).ifPresent(orderId -> {
                Optional<Order> order = ordersRepo.findById(orderId);
                order.get().setStatus(state.getId());
                ordersRepo.save(order.get());
            });
        });
    }
}
