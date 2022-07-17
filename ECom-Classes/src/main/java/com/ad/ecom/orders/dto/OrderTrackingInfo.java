package com.ad.ecom.orders.dto;

import com.ad.ecom.orders.stubs.OrderStatus;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderTrackingInfo {
    private long orderId;
    private List<OrderStatus> trackingInfo;
}
