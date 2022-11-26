package com.ad.ecom.orders.dto;

import com.ad.ecom.products.dto.Item;
import com.ad.ecom.common.dto.EComDate;
import com.sun.istack.NotNull;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderInfo {
    private long orderId;
    private EComDate initDate;
    private EComDate completionDate;
    @NotNull
    private List<Item> items;
    private double subTotal;
    private double total;
    private double refundAmount;
    private long deliveryAddressId;
}