package com.ad.ecom.user.cart.controller;

import com.ad.ecom.common.stub.ResponseMessage;
import com.ad.ecom.user.cart.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/cart")
public class CartController {

    @Autowired
    private CartService cartService;
    @GetMapping("/fetch")
    public ResponseEntity<ResponseMessage> getCart() {
        return cartService.getCart();
    }

    @PostMapping("/update")
    public ResponseEntity<ResponseMessage> updateCart() {
        return cartService.updateCart();
    }
}
