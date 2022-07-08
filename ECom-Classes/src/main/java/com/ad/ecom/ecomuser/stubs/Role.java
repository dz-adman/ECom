package com.ad.ecom.ecomuser.stubs;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public enum Role {
    ROLE_ADMIN(0), ROLE_USER(1), ROLE_SELLER(2);

    private int value;
    private static Map<Integer, Role> map = new HashMap<>();

    private Role(int value) {
        this.value = value;
    }

    static {
        for(Role role : Role.values())
            map.put(role.value, role);
    }

    public static Optional<Role> valueOf(int role) {
        return Optional.ofNullable(map.get(role));
    }

    public Optional<Integer> getIntValue() {
        return Optional.ofNullable(this.value);
    }
}
