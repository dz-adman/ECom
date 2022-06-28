package com.ad.ecom.orders.stubs;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public enum OrderStatus {
    INITIATED(0),
    PENDING_PAYMENT(1),
    PAID(2),
    SHIPPED(3),
    FULFILLED(4),
    CANCELLED(5),
    REFUND_INITIATED(6),
    REFUNDED(7),
    CANCELLED_AND_COMPLETE(8),
    REFUNDED_AND_COMPLETE(9);

    private int value;

    private static Map<Integer, OrderStatus> map = new HashMap<>();

    private OrderStatus(int value) {
        this.value = value;
    }

    static {
        for(OrderStatus orderStatus : OrderStatus.values())
            map.put(orderStatus.value, orderStatus);
    }

    public Optional<OrderStatus> valueOf(int orderStatus) {
        return Optional.ofNullable(map.get(orderStatus));
    }

    public Optional<Integer> getIntValue(OrderStatus orderStatus) {
        return Optional.ofNullable(this.value);
    }
}
