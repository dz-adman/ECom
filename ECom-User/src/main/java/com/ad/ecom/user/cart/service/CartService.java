package com.ad.ecom.user.cart.service;

import com.ad.ecom.common.stub.ResponseMessage;
import com.ad.ecom.user.dto.AddressDto;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public interface CartService {
    public ResponseEntity<ResponseMessage> createCart();
    public ResponseEntity<ResponseMessage> getCart();
    public ResponseEntity<ResponseMessage> updateCart();
    public ResponseEntity<ResponseMessage> getAllAddresses();
    public ResponseEntity<ResponseMessage> updateAddress(AddressDto address);
}
