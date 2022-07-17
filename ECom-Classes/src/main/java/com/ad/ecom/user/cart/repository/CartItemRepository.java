package com.ad.ecom.user.cart.repository;

import com.ad.ecom.user.cart.persistence.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Long> {
}
