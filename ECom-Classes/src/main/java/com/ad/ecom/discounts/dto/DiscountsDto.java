package com.ad.ecom.discounts.dto;

import com.ad.ecom.common.stub.EComDate;
import com.ad.ecom.discounts.stubs.DiscountStatus;
import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class DiscountsDto implements Serializable {

    @NotNull
    private String code;
    @NotNull @NotEmpty @NotBlank
    private String name;
    @NotNull
    private EComDate validFrom;
    @NotNull
    private EComDate validTo;
    @NotNull
    private double percentageValue;
    @NotNull
    private DiscountStatus status;
}
