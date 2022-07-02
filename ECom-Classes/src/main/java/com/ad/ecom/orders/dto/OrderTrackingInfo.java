package com.ad.ecom.orders.dto;

import com.ad.ecom.orders.stubs.OrderStatus;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class OrderTrackingInfo {
    private long orderId;
    private List<OrderStatus> trackingInfo;
}
