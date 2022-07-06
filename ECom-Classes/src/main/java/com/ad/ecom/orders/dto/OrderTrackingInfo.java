package com.ad.ecom.orders.dto;

import com.ad.ecom.orders.stubs.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderTrackingInfo {
    private long orderId;
    private List<OrderStatus> trackingInfo;
}
