package com.ad.ecom.discounts.repository;

import com.ad.ecom.discounts.persistance.DiscountSubscription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DiscountSubscriptionRepository extends JpaRepository<DiscountSubscription, Long> {
    List<DiscountSubscription> findByProductId(long productId);
}
