package com.ad.ecom.user.cart.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CartCheckoutPreview {
    private List<String> discountNames;
    private double subTotal;
    private double total;
}
