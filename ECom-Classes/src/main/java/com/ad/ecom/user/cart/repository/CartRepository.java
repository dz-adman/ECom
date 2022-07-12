package com.ad.ecom.user.cart.repository;

import com.ad.ecom.user.cart.persistance.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {
}
