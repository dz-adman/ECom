package com.ad.ecom.accounts.service.impl;

import com.ad.ecom.accounts.service.UserService;
import com.ad.ecom.common.stub.ResponseMessage;
import com.ad.ecom.user.dto.AddressDto;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
public class UserServiceImpl implements UserService {
    @Override
    public ResponseEntity<ResponseMessage> createCart() {
        // responseMessage 'HttpStatus'
        return null;
    }

    @Override
    public ResponseEntity<ResponseMessage> getCart() {
        // responseData 'List<ProductDto>'
        return null;
    }

    @Override
    public ResponseEntity<ResponseMessage> updateCart() {
        // responseMessage 'HttpStatus'
        return null;
    }

    @Override
    public ResponseEntity<ResponseMessage> getAllAddresses() {
        // responseData 'List<AddressDto>'
        return null;
    }

    @Override
    public ResponseEntity<ResponseMessage> updateAddress(AddressDto address) {
        // responseMessage 'HttpStatus'
        return null;
    }
}
