package com.ad.ecom.user.cart.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CartCheckoutPreview {
    private List<String> discountNames;
    private double subTotal;
    private double total;
}
