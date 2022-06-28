package com.ad.ecom.products.stubs;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public enum ProductStatus {
    ACTIVE(1), INACTIVE(0), OUT_OF_STOCK(2);

    private int value;
    private static Map<Integer, ProductStatus> map = new HashMap<>();

    private ProductStatus(int value) {
        this.value = value;
    }

    static {
        for(ProductStatus productStatus : ProductStatus.values())
            map.put(productStatus.value, productStatus);
    }

    private Optional<ProductStatus> valueOf(int productStatus) {
        return Optional.ofNullable(map.get(productStatus));
    }

    private Optional<Integer> getIntValue(ProductStatus productStatus) {
        return Optional.ofNullable(productStatus.value);
    }
}
