package com.ad.ecom.user.cart.service;

import com.ad.ecom.common.stub.ResponseMessage;
import com.ad.ecom.user.profile.dto.AddressDto;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public interface CartService {
    ResponseEntity<ResponseMessage> createCart();
    ResponseEntity<ResponseMessage> getCart();
    ResponseEntity<ResponseMessage> updateCartItems();
    ResponseEntity<ResponseMessage> changeDeliveryAddress(AddressDto address);
}
