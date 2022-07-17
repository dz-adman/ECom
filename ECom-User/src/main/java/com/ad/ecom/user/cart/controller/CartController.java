package com.ad.ecom.user.cart.controller;

import com.ad.ecom.common.ResponseMessage;
import com.ad.ecom.user.cart.dto.CartInfo;
import com.ad.ecom.user.cart.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;

@RestController
@RequestMapping("/cart")
public class CartController {

    @Autowired
    private CartService cartService;

    @RolesAllowed("USER")
    @GetMapping("/fetch")
    public ResponseEntity<ResponseMessage> getCart() {
        return cartService.getCart();
    }

    @RolesAllowed("USER")
    @PatchMapping("/update/items")
    public ResponseEntity<ResponseMessage> updateCartItems(@RequestBody CartInfo cartInfo) {
        return cartService.updateCartItems(cartInfo);
    }

    @RolesAllowed("USER")
    @GetMapping("/update/deliveryAddress/{addressId}")
    public ResponseEntity<ResponseMessage> changeDeliveryAddress(@PathVariable("addressId") long addressId) {
        return cartService.changeDeliveryAddress(addressId);
    }

    @RolesAllowed("USER")
    @GetMapping("/checkout/preview")
    public ResponseEntity<ResponseMessage> checkoutPreview() {
        return cartService.checkoutPreview();
    }
}
