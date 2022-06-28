package com.ad.ecom.products.stubs;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public enum ProductSubCategory {
    ELECTRONICS_MOBILE(0),
    ELECTRONICS_TABLET(1),
    ELECTRONICS_FRIDGE(2),
    ELECTRONICS_TV(3),
    ELECTRONICS_WASHINGMACHINE(4),
    FASHION_MENSFASHION(5),
    FASHION_WOMENSFASHION(6),
    OTHER(7);

    private int value;
    private static Map<Integer, ProductSubCategory> map = new HashMap<>();

    private ProductSubCategory(int value) {
        this.value = value;
    }

    static {
        for(ProductSubCategory productSubCategory : ProductSubCategory.values())
            map.put(productSubCategory.value, productSubCategory);
    }

    private Optional<ProductSubCategory> valueOf(int productSubCategory) {
        return Optional.ofNullable(map.get(productSubCategory));
    }

    private Optional<Integer> getIntValue(ProductSubCategory productSubCategory) {
        return Optional.ofNullable(productSubCategory.value);
    }
}
