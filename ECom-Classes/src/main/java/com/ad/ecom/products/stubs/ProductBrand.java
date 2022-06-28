package com.ad.ecom.products.stubs;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public enum ProductBrand {
    A(0), B(1), OTHER(2);

    private int value;
    private static Map<Integer, ProductBrand> map = new HashMap<>();

    private ProductBrand(int value) {
        this.value = value;
    }

    static {
        for(ProductBrand productBrand : ProductBrand.values())
            map.put(productBrand.value, productBrand);
    }

    private Optional<ProductBrand> valueOf(int productBrand) {
        return Optional.ofNullable(map.get(productBrand));
    }

    private Optional<Integer> getIntValue(ProductBrand productBrand) {
        return Optional.ofNullable(productBrand.value);
    }
}
