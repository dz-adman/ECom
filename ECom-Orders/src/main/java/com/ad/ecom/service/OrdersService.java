package com.ad.ecom.service;

import com.ad.ecom.common.stub.ResponseMessage;
import com.ad.ecom.orders.dto.OrderInfo;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public interface OrdersService {
    ResponseEntity<ResponseMessage> initiateOrder(OrderInfo orderInfo);
    ResponseEntity<ResponseMessage> orderInfo(long orderId);
    ResponseEntity<ResponseMessage> orderPayment(long orderId);
    ResponseEntity<ResponseMessage> initiateShipment(long orderId);
    ResponseEntity<ResponseMessage> cancelOrder(long orderId);
    ResponseEntity<ResponseMessage> trackOrder(long orderId);
    ResponseEntity<ResponseMessage> deliverOrder(long orderId);
    ResponseEntity<ResponseMessage> markManualRefund(long orderId);
}
