package com.ad.ecom.accounts.controller;

import com.ad.ecom.accounts.service.UserService;
import com.ad.ecom.common.stub.ResponseMessage;
import com.ad.ecom.user.dto.AddressDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/address")
    public ResponseEntity<ResponseMessage> getAddress() {
        return userService.getAllAddresses();
    }

    @PatchMapping("/address")
    public ResponseEntity<ResponseMessage> updateAddress(AddressDto address) {
        return userService.updateAddress(address);
    }

    @GetMapping("/cart")
    public ResponseEntity<ResponseMessage> getCart() {
        return userService.getCart();
    }

    @PostMapping("/cart")
    public ResponseEntity<ResponseMessage> updateCart() {
        return userService.updateCart();
    }

}
