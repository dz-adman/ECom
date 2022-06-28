package com.ad.ecom.discounts.stubs;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public enum DiscountStatus {
    ACTIVE(1), INACTIVE(0);

    private int value;
    private static Map<Integer, DiscountStatus> map = new HashMap<>();

    private DiscountStatus(int value) {
        this.value = value;
    }

    static {
        for(DiscountStatus discountStatus : DiscountStatus.values())
            map.put(discountStatus.value, discountStatus);
    }

    private Optional<DiscountStatus> valueOf(int discountStatus) {
        return Optional.ofNullable(map.get(discountStatus));
    }

    private Optional<Integer> getIntValue(DiscountStatus discountStatus) {
        return Optional.ofNullable(discountStatus.value);
    }
}
