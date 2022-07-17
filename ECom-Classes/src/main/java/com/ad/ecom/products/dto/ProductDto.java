package com.ad.ecom.products.dto;

import com.ad.ecom.products.stubs.ProductBrand;
import com.ad.ecom.products.stubs.ProductCategory;
import com.ad.ecom.products.stubs.ProductStockUnit;
import com.ad.ecom.products.stubs.ProductSubCategory;
import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductDto implements Serializable {
    private String productId;
    private String name;
    private ProductCategory category;
    private ProductSubCategory subCategory;
    private ProductBrand brand;
    /** @param price original price */
    private double price;
    /** @param effectivePrice price after applying all the subscribed discounts */
    private double effectivePrice;
    private boolean inStock;
    private ProductStockUnit stockUnit;
}
