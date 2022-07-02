package com.ad.ecom.orders.repository;

import com.ad.ecom.orders.persistance.OrderItems;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderItemsRepository extends JpaRepository<OrderItems, Long> {
}
