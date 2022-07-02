package com.ad.ecom.orders.dto;

import com.ad.ecom.common.dto.Item;
import com.ad.ecom.common.stub.EComDate;
import com.sun.istack.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
public class OrderInfo {
    private long orderId;
    private EComDate initDate;
    private EComDate completionDate;
    @NotNull
    private List<Item> items;
    private double subTotal;
    private double total;
    private double refundAmount;
    @NotNull
    private long deliveryAddressId;
}
