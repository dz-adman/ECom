package com.ad.ecom.user.cart.dto;

import com.ad.ecom.common.dto.Item;
import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CartInfo {
    private long cartId;
    @NotNull
    private List<Item> items;
}