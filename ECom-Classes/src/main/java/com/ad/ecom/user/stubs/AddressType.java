package com.ad.ecom.user.stubs;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public enum AddressType {
    RESIDENTIAL(0), OFFICIAL(1), BUSINESS(2), OTHER(3);

    private int value;

    private static Map<Integer, AddressType> map = new HashMap<>();

    private AddressType(int value) {
        this.value = value;
    }

    static{
        for(AddressType addressType : AddressType.values())
            map.put(addressType.value, addressType);
    }

    public Optional<AddressType> valueOf(int addressType) {
        return Optional.ofNullable(map.get(addressType));
    }

    public Optional<Integer> getIntValue() {
        return Optional.ofNullable(this.value);
    }
}
