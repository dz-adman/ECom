package com.ad.ecom.orders.dto;

import com.ad.ecom.common.dto.Item;
import com.ad.ecom.common.stub.EComDate;
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