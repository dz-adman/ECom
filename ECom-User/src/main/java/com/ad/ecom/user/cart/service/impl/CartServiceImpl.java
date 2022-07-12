package com.ad.ecom.user.cart.service.impl;

import com.ad.ecom.common.stub.ResponseMessage;
import com.ad.ecom.user.cart.service.CartService;
import com.ad.ecom.user.profile.dto.AddressDto;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
public class CartServiceImpl implements CartService {

    @Override
    public ResponseEntity<ResponseMessage> createCart() {
        return null;
    }

    @Override
    public ResponseEntity<ResponseMessage> getCart() {
        return null;
    }

    @Override
    public ResponseEntity<ResponseMessage> updateCartItems() {
        return null;
    }

    @Override
    public ResponseEntity<ResponseMessage> changeDeliveryAddress(AddressDto address) {
        return null;
    }
}
