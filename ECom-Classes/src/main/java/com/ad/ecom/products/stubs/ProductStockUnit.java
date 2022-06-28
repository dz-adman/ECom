package com.ad.ecom.products.stubs;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public enum ProductStockUnit {
    NUMBER(0), KILOGRAM(1), GRAM(2), POUND(3), LBS(4), LITRE(5);

    private int value;
    private static Map<Integer, ProductStockUnit> map = new HashMap<>();

    private ProductStockUnit(int value) {
        this.value = value;
    }

    static {
        for(ProductStockUnit productStockUnit : ProductStockUnit.values())
            map.put(productStockUnit.value, productStockUnit);
    }

    private Optional<ProductStockUnit> valueOf(int productStockUnit) {
        return Optional.ofNullable(map.get(productStockUnit));
    }

    private Optional<Integer> getIntValue(ProductStockUnit productStockUnit) {
        return Optional.ofNullable(productStockUnit.value);
    }
}
