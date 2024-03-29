package com.ad.ecom.util.emailEvent;

import com.ad.ecom.ecomuser.persistance.EComUser;
import com.ad.ecom.orders.stubs.OrderStatus;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class OrderStatusUpdateEmailEvent extends ApplicationEvent {
    private EComUser user;
    private long orderId;
    private OrderStatus orderStatus;

    public OrderStatusUpdateEmailEvent(Object source, EComUser user, long orderId, OrderStatus orderStatus) {
        super(source);
        this.user = user;
        this.orderId = orderId;
        this.orderStatus = orderStatus;
    }
}
