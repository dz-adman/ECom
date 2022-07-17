package com.ad.ecom.user.cart.service.impl;

import com.ad.ecom.common.ResponseMessage;
import com.ad.ecom.common.dto.Item;
import com.ad.ecom.common.stub.ResponseType;
import com.ad.ecom.core.context.EComUserLoginContext;
import com.ad.ecom.discounts.persistence.DiscountSubscription;
import com.ad.ecom.discounts.repository.DiscountSubscriptionRepository;
import com.ad.ecom.products.persistence.Product;
import com.ad.ecom.products.repository.ProductRepository;
import com.ad.ecom.user.cart.dto.CartCheckoutPreview;
import com.ad.ecom.user.cart.dto.CartInfo;
import com.ad.ecom.user.cart.persistence.Cart;
import com.ad.ecom.user.cart.persistence.CartItem;
import com.ad.ecom.user.cart.repository.CartRepository;
import com.ad.ecom.user.cart.service.CartService;
import com.ad.ecom.user.profile.persistence.Address;
import com.ad.ecom.user.profile.repository.AddressRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class CartServiceImpl implements CartService {

    @Autowired
    private EComUserLoginContext loginContext;
    @Autowired
    private CartRepository cartRepo;
    @Autowired
    private AddressRepository addressRepo;
    @Autowired
    private DiscountSubscriptionRepository discountSubsRepo;
    @Autowired
    private ProductRepository productRepo;

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

        Optional<Address> address = addressRepo.findByUserIdAndId(loginContext.getUserInfo().getId(), addressId);
        if(address.isEmpty()) {
            responseMessage.addResponse(ResponseType.ERROR, "Invalid AddressId");
            return new ResponseEntity<>(responseMessage, HttpStatus.BAD_REQUEST);
        }
        Cart cart = fetchCartOrCreateIfAbsent();
        cart.setDeliveryAddress(addressId);
        cartRepo.save(cart);

        responseMessage.addResponse(ResponseType.SUCCESS, "Delivery Address Updated Successfully");
        return new ResponseEntity<>(responseMessage, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<ResponseMessage> checkoutPreview() {
        ResponseMessage responseMessage = new ResponseMessage();

        Cart cart = fetchCartOrCreateIfAbsent();
        Map<CartItem, Product> itemProductMap = new HashMap<>();
        for (CartItem cartItem : cart.getItems()) {
            Optional<Product> product = productRepo.findByProductId(cartItem.getItemProductId());
            itemProductMap.put(cartItem, product.get());
        }
        double subTotal = itemProductMap.entrySet().stream().mapToDouble(e -> e.getValue().getPrice() * e.getKey().getItemQuantity()).sum();
        List<DiscountSubscription> allSubs = new ArrayList<>();
        double total = 0.0;
        for (Map.Entry<CartItem, Product> e : itemProductMap.entrySet()) {
            List<DiscountSubscription> subscriptions = e.getValue().getDiscountsApplicableOnProduct(discountSubsRepo);
            allSubs.addAll(subscriptions);
            total += (e.getValue().getPrice() - e.getValue().getDiscountOnProduct(discountSubsRepo)) * e.getKey().getItemQuantity();
        }
        List<String> discountNames = allSubs.stream().map(subs -> subs.getDiscount().getName()).collect(Collectors.toList());

        CartCheckoutPreview preview = CartCheckoutPreview.builder()
                                                         .subTotal(subTotal)
                                                         .total(total)
                                                         .discountNames(discountNames)
                                                         .build();
        responseMessage.addResponse(ResponseType.SUCCESS, "Cart Checkout Preview");
        responseMessage.setResponseData(preview);
        return new ResponseEntity<>(responseMessage, HttpStatus.OK);
    }

    private void addItemsToCart(Cart cart, List<Item> items) {
        if(items.isEmpty()) {
            cart.setItems(Collections.emptyList());
            return;
        }
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
        long defaultAddress = addressRepo.findByUserIdAndDefaultAddressTrue(cart.getUserId()).get().getId();
        long deliveryAddress = Optional.ofNullable(cart.getDeliveryAddress()).isPresent() ? cart.getDeliveryAddress() : defaultAddress;
        if(cart.getItems() == null) return CartInfo.builder()
                                                   .cartId(cart.getId())
                                                   .items(Collections.emptyList())
                                                   .deliveryAddress(deliveryAddress).build();
        List<Item> items = cart.getItems().stream().map(item ->
            Item.builder()
                .itemProductName(item.getItemProductName())
                .itemProductId(item.getItemProductId())
                .itemQuantity(item.getItemQuantity())
                .itemUnit(item.getItemUnit()).build()).collect(Collectors.toList());
        return CartInfo.builder().cartId(cart.getId()).items(items).deliveryAddress(deliveryAddress).build();
    }
}