package com.ad.ecom.user.cart.service;

import com.ad.ecom.common.ResponseMessage;
import com.ad.ecom.user.cart.dto.CartInfo;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public interface CartService {
    ResponseEntity<ResponseMessage> getCart();
    ResponseEntity<ResponseMessage> updateCartItems(CartInfo cartInfo);
    ResponseEntity<ResponseMessage> changeDeliveryAddress(long addressId);
    ResponseEntity<ResponseMessage> checkoutPreview();
}
