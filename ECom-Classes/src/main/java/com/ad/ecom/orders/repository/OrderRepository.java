package com.ad.ecom.orders.repository;

import com.ad.ecom.orders.persistance.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    Optional<Order> findByUserIdAndOrderId(long userId, long orderId);
}
