package com.ad.ecom.support.stubs;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public enum TicketStatus {
    OPEN(0), IN_PROCESS(1), SOLVED_CLOSED(3), HARD_CLOSED(4);

    private int value;
    private static Map<Integer, TicketStatus> map = new HashMap<>();

    private TicketStatus(int value) {
        this.value = value;
    }

    static {
        for(TicketStatus ticketStatus : TicketStatus.values())
            map.put(ticketStatus.value, ticketStatus);
    }

    public Optional<TicketStatus> valueOf(int ticketStatus) {
        return Optional.ofNullable(map.get(ticketStatus));
    }

    public Optional<Integer> getIntValue(TicketStatus ticketStatus) {
        return Optional.ofNullable(this.value);
    }
}
