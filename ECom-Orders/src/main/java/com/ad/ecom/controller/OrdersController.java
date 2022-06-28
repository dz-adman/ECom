package com.ad.ecom.controller;

import com.ad.ecom.common.stub.ResponseMessage;
import com.ad.ecom.common.stub.ResponseType;
import com.ad.ecom.orders.dto.OrderInfo;
import com.ad.ecom.orders.persistance.Order;
import com.ad.ecom.orders.repository.OrdersRepository;
import com.ad.ecom.service.OrdersService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;
import javax.validation.constraints.NotNull;
import java.util.Optional;

@RestController
@RequestMapping("/orders")
public class OrdersController {
    private static final Logger LOGGER = LogManager.getLogger(OrdersController.class);
    @Autowired
    private OrdersService ordersService;

    @Autowired
    private OrdersRepository ordersRepo;

    @RolesAllowed("USER")
    @PostMapping("/init")
    public ResponseEntity<ResponseMessage> initiateOrder(@RequestBody @NotNull OrderInfo orderInfo) {
        return ordersService.initiateOrder(orderInfo);
    }

    @RolesAllowed("")
    @GetMapping("/info/{orderId}")
    public ResponseEntity<ResponseMessage> orderInfo(@PathVariable long orderId) {
        return ordersService.orderInfo(orderId);
    }

    @RolesAllowed("USER")
    @GetMapping("/track/{orderId}")
    public ResponseEntity<ResponseMessage> trackOrder(@PathVariable long orderId) {
        Optional<Order> order = ordersRepo.findById(orderId);
        LOGGER.info(order.isPresent() ? "Order State: " + order.get().getStatus() : "ORDER NOT FOUND");
        ResponseMessage response = new ResponseMessage();
        response.addResponse(ResponseType.SUCCESS, order.get().getStatus().toString());
        return new ResponseEntity(response, HttpStatus.OK);
    }

    @RolesAllowed(("USER"))
    @GetMapping("/payment/{orderId}")
    public ResponseEntity<ResponseMessage> orderPayment(@PathVariable long orderId) {
        return ordersService.orderPayment(orderId);
    }

    @GetMapping("/init-shipment")
    public ResponseEntity<ResponseMessage> initShipment(@PathVariable long orderId) {
        return ordersService.initiateShipment(orderId);
    }

}
