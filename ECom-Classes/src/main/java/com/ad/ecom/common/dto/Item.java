package com.ad.ecom.common.dto;

import com.ad.ecom.products.stubs.ProductStockUnit;
import lombok.*;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Item implements Serializable {
    @NotNull @NotEmpty @NotBlank
    private String itemProductId;

    private String itemProductName;

    @NotNull @Min(1)
    private long itemQuantity;

    @NotNull
    private ProductStockUnit itemUnit;
}
