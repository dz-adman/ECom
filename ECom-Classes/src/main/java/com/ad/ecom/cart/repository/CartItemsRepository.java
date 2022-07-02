package com.ad.ecom.cart.repository;

import com.ad.ecom.cart.persistance.CartItems;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CartItemsRepository extends JpaRepository<CartItems, Long> {
}
