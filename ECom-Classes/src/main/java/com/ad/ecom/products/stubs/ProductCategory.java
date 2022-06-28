package com.ad.ecom.products.stubs;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public enum ProductCategory {
    ElECTRONICS(0), FASHION(1), OTHER(2);

    private int value;
    private static Map<Integer, ProductCategory> map = new HashMap<>();

    private ProductCategory(int value) {
        this.value = value;
    }

    static {
        for(ProductCategory productCategory : ProductCategory.values())
            map.put(productCategory.value, productCategory);
    }

    private Optional<ProductCategory> valueOf(int productCategory) {
        return Optional.ofNullable(map.get(productCategory));
    }

    private Optional<Integer> getIntValue(ProductCategory productCategory) {
        return Optional.ofNullable(productCategory.value);
    }
}
