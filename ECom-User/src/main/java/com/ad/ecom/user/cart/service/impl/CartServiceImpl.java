package com.ad.ecom.user.cart.service.impl;

import com.ad.ecom.common.ResponseMessage;
import com.ad.ecom.common.dto.Item;
import com.ad.ecom.common.stub.ResponseType;
import com.ad.ecom.core.context.EComUserLoginContext;
import com.ad.ecom.user.cart.dto.CartInfo;
import com.ad.ecom.user.cart.persistance.Cart;
import com.ad.ecom.user.cart.persistance.CartItem;
import com.ad.ecom.user.cart.repository.CartRepository;
import com.ad.ecom.user.cart.service.CartService;
import com.ad.ecom.user.profile.persistance.Address;
import com.ad.ecom.user.profile.repository.AddressRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class CartServiceImpl implements CartService {

    @Autowired
    private EComUserLoginContext loginContext;
    @Autowired
    private CartRepository cartRepo;
    @Autowired
    private AddressRepository addressRepo;

    @Override
    public ResponseEntity<ResponseMessage> getCart() {
        ResponseMessage responseMessage = new ResponseMessage();

        Cart cart = fetchCartOrCreateIfAbsent();
        CartInfo cartInfo = cartToCartInfo(cart);

        responseMessage.addResponse(ResponseType.SUCCESS, "Your Cart-Info");
        responseMessage.setResponseData(cartInfo);
        return new ResponseEntity<>(responseMessage, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<ResponseMessage> updateCartItems(CartInfo cartInfo) {
        ResponseMessage responseMessage = new ResponseMessage();

        Cart cart = fetchCartOrCreateIfAbsent();
        addItemsToCart(cart, cartInfo.getItems());
        cartRepo.save(cart);

        responseMessage.addResponse(ResponseType.SUCCESS, "Cart updated Successfully");
        return new ResponseEntity<>(responseMessage, HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<ResponseMessage> changeDeliveryAddress(long addressId) {
        ResponseMessage responseMessage = new ResponseMessage();

        Cart cart = fetchCartOrCreateIfAbsent();
        cart.setDeliveryAddress(addressId);
        cartRepo.save(cart);

        responseMessage.addResponse(ResponseType.SUCCESS, "Delivery Address Updated Successfully");
        return new ResponseEntity<>(responseMessage, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<ResponseMessage> checkoutPreview() {
        return null;
    }

    private void addItemsToCart(Cart cart, List<Item> items) {
        List<CartItem> cartItems = items.stream().map(item ->
                CartItem.builder()
                        .cart(cart)
                        .itemProductId(item.getItemProductId())
                        .itemProductName(item.getItemProductName())
                        .itemQuantity(item.getItemQuantity())
                        .itemUnit(item.getItemUnit()).build()).collect(Collectors.toList());
        cart.setItems(cartItems);
    }

    private Cart fetchCartOrCreateIfAbsent() {
        Optional<Cart> cartData = cartRepo.findByUserId(loginContext.getUserInfo().getId());
        if(cartData.isEmpty()) {
            Optional<Address> addr = addressRepo.findByUserIdAndDefaultAddressTrue(loginContext.getUserInfo().getId());
            Cart newCart = Cart.builder().userId(loginContext.getUserInfo().getId()).deliveryAddress(addr.get().getId()).build();
            cartRepo.save(newCart);
            return newCart;
        }
        return cartData.get();
    }

    private CartInfo cartToCartInfo(Cart cart) {
        List<Item> items = cart.getItems().stream().map(item ->
            Item.builder()
                .itemProductName(item.getItemProductName())
                .itemProductId(item.getItemProductId())
                .itemQuantity(item.getItemQuantity())
                .itemUnit(item.getItemUnit()).build()).collect(Collectors.toList());
        return CartInfo.builder().cartId(cart.getId()).items(items).build();
    }
}