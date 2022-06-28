package com.ad.ecom.config;

import com.ad.ecom.orders.stubs.OrderEvent;
import com.ad.ecom.orders.stubs.OrderStatus;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.annotation.Configuration;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.config.EnableStateMachineFactory;
import org.springframework.statemachine.config.StateMachineConfigurerAdapter;
import org.springframework.statemachine.config.builders.StateMachineConfigurationConfigurer;
import org.springframework.statemachine.config.builders.StateMachineStateConfigurer;
import org.springframework.statemachine.config.builders.StateMachineTransitionConfigurer;
import org.springframework.statemachine.listener.StateMachineListenerAdapter;
import org.springframework.statemachine.state.State;

import java.util.EnumSet;

@Configuration
@EnableStateMachineFactory
public class EComOrdersSMConfig extends StateMachineConfigurerAdapter<OrderStatus, OrderEvent> {

    private static final Logger LOGGER = LogManager.getLogger(EComOrdersSMConfig.class);

    @Override
    public void configure(StateMachineStateConfigurer<OrderStatus, OrderEvent> states) throws Exception {
        states
                .withStates()
                .initial(OrderStatus.INITIATED)
                .end(OrderStatus.CANCELLED_AND_COMPLETE)
                .end(OrderStatus.REFUNDED_AND_COMPLETE)
                .end(OrderStatus.FULFILLED)
                .states(EnumSet.allOf(OrderStatus.class))
                .and();
    }

    @Override
    public void configure(StateMachineTransitionConfigurer<OrderStatus, OrderEvent> transitions) throws Exception {
        transitions
                .withExternal()
                                .source(OrderStatus.INITIATED)
                                .target(OrderStatus.PENDING_PAYMENT)
                                .event(OrderEvent.PROCESS_ORDER)
                .and()
                .withExternal()
                                .source(OrderStatus.PENDING_PAYMENT)
                                .target(OrderStatus.PENDING_PAYMENT)
                                .event(OrderEvent.PAYMENT_FAILED)
                .and()
                .withExternal()
                                .source(OrderStatus.PENDING_PAYMENT)
                                .target(OrderStatus.PAID)
                                .event(OrderEvent.PAYMENT_SUCCESSFUL)
                .and()
                .withExternal()
                                .source(OrderStatus.PAID)
                                .target(OrderStatus.SHIPPED)
                                .event(OrderEvent.INIT_SHIPMENT)
                .and()
                .withExternal()
                                .source(OrderStatus.SHIPPED)
                                .target(OrderStatus.FULFILLED)
                                .event(OrderEvent.DELIVERED)
                .and()
                .withExternal()
                                .source(OrderStatus.INITIATED)
                                .target(OrderStatus.CANCELLED)
                                .event(OrderEvent.CANCEL_ORDER)
                .and()
                .withExternal()
                                .source(OrderStatus.PENDING_PAYMENT)
                                .target(OrderStatus.CANCELLED)
                                .event(OrderEvent.CANCEL_ORDER)
                .and()
                .withExternal()
                                .source(OrderStatus.PAID)
                                .target(OrderStatus.CANCELLED)
                                .event(OrderEvent.CANCEL_ORDER)
                .and()
                .withExternal()
                                .source(OrderStatus.CANCELLED)
                                .target(OrderStatus.CANCELLED_AND_COMPLETE)
                                .event(OrderEvent.COMPLETE)
                .and()
                .withExternal()
                                .source(OrderStatus.CANCELLED)
                                .target(OrderStatus.REFUND_INITIATED)
                                .event(OrderEvent.INIT_REFUND)
                .and()
                .withExternal()
                                .source(OrderStatus.REFUND_INITIATED)
                                .target(OrderStatus.REFUNDED)
                                .event(OrderEvent.REFUND_COMPLETE)
                .and()
                .withExternal()
                                .source(OrderStatus.REFUNDED)
                                .target(OrderStatus.REFUNDED_AND_COMPLETE)
                                .event(OrderEvent.COMPLETE);
    }

    @Override
    public void configure(StateMachineConfigurationConfigurer<OrderStatus, OrderEvent> config) throws Exception {
        StateMachineListenerAdapter<OrderStatus, OrderEvent> ssmListener = new StateMachineListenerAdapter<>() {
            @Override
            public void stateChanged(State<OrderStatus, OrderEvent> from, State<OrderStatus, OrderEvent> to) {
                LOGGER.info(String.format("State changed from %s to %s", from.getId(), to.getId()));
            }
            @Override
            public void stateMachineError(StateMachine<OrderStatus, OrderEvent> stateMachine, Exception exception) {
                LOGGER.error(String.format("StateMachine(%s) Exception : %s", stateMachine.getId(), exception));
            }
        };
        config.withConfiguration()
              .autoStartup(false)
              .listener(ssmListener);
    }

}
