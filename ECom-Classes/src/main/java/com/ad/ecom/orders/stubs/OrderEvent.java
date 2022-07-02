package com.ad.ecom.orders.stubs;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public enum OrderEvent {
    PROCESS_ORDER(0),
    PAYMENT_SUCCESSFUL(1),
    PAYMENT_FAILED(2),
    INIT_SHIPMENT(3),
    CANCEL_ORDER(4),
    REFUND_COMPLETE(5),
    REFUND_FAILED(6),
    COMPLETE(7),
    DELIVERED(8),
    INIT_REFUND(9);

    private int value;

    private static Map<Integer, OrderEvent> map = new HashMap<>();

    private OrderEvent(int value) {
        this.value = value;
    }

    static {
        for(OrderEvent orderEvent : OrderEvent.values())
            map.put(orderEvent.value, orderEvent);
    }

    public Optional<OrderEvent> valueOf(int orderEvent) {
        return Optional.ofNullable(map.get(orderEvent));
    }

    public Optional<Integer> getIntValue(OrderEvent orderEvent) {
        return Optional.ofNullable(this.value);
    }
}
