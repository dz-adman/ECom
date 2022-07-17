package com.ad.ecom.user.cart.dto;

import com.ad.ecom.common.dto.Item;
import com.sun.istack.NotNull;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CartInfo {
    private long cartId;
    @NotNull
    private List<Item> items;
    private long deliveryAddress;
}