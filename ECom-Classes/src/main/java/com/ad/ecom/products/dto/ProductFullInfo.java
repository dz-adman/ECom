package com.ad.ecom.products.dto;

import com.ad.ecom.products.stubs.*;
import lombok.*;

import javax.validation.constraints.*;
import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductFullInfo implements Serializable {
    @NotNull
    private String productId;
    @NotNull @NotEmpty @NotBlank
    private String name;
    @NotNull
    private ProductCategory category;
    @NotNull
    private ProductSubCategory subCategory;
    @NotNull
    private ProductBrand brand;
    @NotNull @DecimalMin(value = "1.0", message = "Price too low")
    private double price;
    @NotNull @Min(0)
    private long stock;
    @NotNull
    private ProductStockUnit stockUnit;
    @NotNull
    private ProductStatus status;
}
