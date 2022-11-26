package com.ad.ecom.support.stubs;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public enum TicketEvent {
    OPEN_REQUEST(0), INIT_PROCESSING(1), SOLVED_CLOSURE(3), HARD_CLOSURE(4);

    private int value;
    private static Map<Integer, TicketEvent> map = new HashMap<>();

    private TicketEvent(int value) {
        this.value = value;
    }

    static {
        for(TicketEvent ticketEvent : TicketEvent.values())
            map.put(ticketEvent.value, ticketEvent);
    }

    public Optional<TicketEvent> valueOf(int ticketEvent) {
        return Optional.ofNullable(map.get(ticketEvent));
    }

    public Optional<Integer> getIntValue(TicketEvent ticketEvent) {
        return Optional.ofNullable(this.value);
    }
}
