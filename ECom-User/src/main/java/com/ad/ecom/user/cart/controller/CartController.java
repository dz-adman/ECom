package com.ad.ecom.user.cart.controller;

import com.ad.ecom.common.ResponseMessage;
import com.ad.ecom.user.cart.service.CartService;
import com.ad.ecom.user.profile.dto.AddressDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
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

    @PatchMapping("/update/cartItems")
    public ResponseEntity<ResponseMessage> updateCartItems() {
        return cartService.updateCartItems();
    }

    @PatchMapping("/update/deliveryAddress")
    public ResponseEntity<ResponseMessage> changeDeliveryAddress(AddressDto addressDto) {
        return cartService.changeDeliveryAddress(addressDto);
    }
}
