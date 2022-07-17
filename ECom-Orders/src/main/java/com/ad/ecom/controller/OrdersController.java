package com.ad.ecom.controller;

import com.ad.ecom.common.ResponseMessage;
import com.ad.ecom.orders.dto.OrderInfo;
import com.ad.ecom.orders.repository.OrderRepository;
import com.ad.ecom.service.OrdersService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;
import javax.validation.constraints.NotNull;

@RestController
@RequestMapping("/orders")
public class OrdersController {
    private static final Logger LOGGER = LogManager.getLogger(OrdersController.class);
    @Autowired
    private OrdersService ordersService;

    @Autowired
    private OrderRepository ordersRepo;

    @RolesAllowed("USER")
    @PostMapping("/init")
    public ResponseEntity<ResponseMessage> initiateOrder(@RequestBody @NotNull OrderInfo orderInfo) {
        return ordersService.initiateOrder(orderInfo);
    }

    @RolesAllowed({"USER", "ADMIN"})
    @GetMapping("/info/{orderId}")
    public ResponseEntity<ResponseMessage> orderInfo(@PathVariable long orderId) {
        return ordersService.orderInfo(orderId);
    }

    @RolesAllowed({"USER", "ADMIN"})
    @GetMapping("/track/{orderId}")
    public ResponseEntity<ResponseMessage> trackOrder(@PathVariable long orderId) {
        return ordersService.trackOrder(orderId);
    }

    @RolesAllowed("USER")
    @GetMapping("/payment/{orderId}")
    public ResponseEntity<ResponseMessage> orderPayment(@PathVariable long orderId) {
        return ordersService.orderPayment(orderId);
    }

    @RolesAllowed("ADMIN")
    @GetMapping("/initShipment/{orderId}")
    public ResponseEntity<ResponseMessage> initShipment(@PathVariable long orderId) {
        return ordersService.initiateShipment(orderId);
    }

    @RolesAllowed("USER")
    @GetMapping("/cancel/{orderId}")
    public ResponseEntity<ResponseMessage> cancelOrder(@PathVariable long orderId) {
        return ordersService.cancelOrder(orderId);
    }

    @RolesAllowed("ADMIN")
    @GetMapping("/deliver/{orderId}")
    public ResponseEntity<ResponseMessage> deliverOrder(@PathVariable long orderId) {
        return ordersService.deliverOrder(orderId);
    }

    @RolesAllowed("ADMIN")
    @GetMapping("/markManualRefund/{orderId}")
    public ResponseEntity<ResponseMessage> markManualRefund(@PathVariable long orderId) {
        return ordersService.markManualRefund(orderId);
    }
}
