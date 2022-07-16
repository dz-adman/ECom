package com.ad.ecom.user.cart.controller;

import com.ad.ecom.common.ResponseMessage;
import com.ad.ecom.user.cart.dto.CartInfo;
import com.ad.ecom.user.cart.service.CartService;
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
    public ResponseEntity<ResponseMessage> updateCartItems(CartInfo cartInfo) {
        return cartService.updateCartItems(cartInfo);
    }

    @PatchMapping("/update/deliveryAddress")
    public ResponseEntity<ResponseMessage> changeDeliveryAddress(long addressId) {
        return cartService.changeDeliveryAddress(addressId);
    }

    @GetMapping("/checkout/preview")
    public ResponseEntity<ResponseMessage> checkoutPreview() {
        return cartService.checkoutPreview();
    }
}
