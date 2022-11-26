package com.ad.ecom.discounts.dto;

import com.ad.ecom.common.dto.EComDate;
import com.ad.ecom.discounts.stubs.DiscountStatus;
import com.ad.ecom.common.dto.DataRange;
import lombok.*;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DiscountsFilter implements Serializable {
    private List<String> discountCodes;
    private List<String> discountNames;
    private DataRange<EComDate> validFromRange;
    private DataRange<EComDate> validToRange;
    private DataRange<Double> discountPercentageRange;
    private DiscountStatus discountStatus;
}
