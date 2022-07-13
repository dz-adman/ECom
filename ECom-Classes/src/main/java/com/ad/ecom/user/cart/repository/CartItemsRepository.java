package com.ad.ecom.user.cart.repository;

import com.ad.ecom.user.cart.persistance.CartItems;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CartItemsRepository extends JpaRepository<CartItems, Long> {
}
