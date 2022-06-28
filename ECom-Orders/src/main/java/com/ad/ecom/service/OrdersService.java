package com.ad.ecom.service;

import com.ad.ecom.common.stub.ResponseMessage;
import com.ad.ecom.orders.dto.OrderInfo;
import com.ad.ecom.orders.stubs.OrderEvent;
import com.ad.ecom.orders.stubs.OrderStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.statemachine.StateMachine;
import org.springframework.stereotype.Service;

@Service
public interface OrdersService {
    public StateMachine<OrderStatus, OrderEvent> build(long orderId);
    public ResponseEntity<ResponseMessage> initiateOrder(OrderInfo orderInfo);
    public ResponseEntity<ResponseMessage> orderInfo(long orderId);
    public ResponseEntity<ResponseMessage> orderPayment(long orderId);
    public ResponseEntity<ResponseMessage> initiateShipment(long orderId);
    public ResponseEntity<ResponseMessage> cancelOrder();
    public ResponseEntity<ResponseMessage> trackOrder();
}
