package com.ad.ecom.products.repository;

import com.ad.ecom.products.persistance.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long>, QuerydslPredicateExecutor<Product> {
    Optional<Product> findByProductId(String productId);
    void deleteByProductId(String productId);
}
