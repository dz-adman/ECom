package com.ad.ecom.products.repository;

import com.ad.ecom.products.persistance.Products;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProductsRepository extends JpaRepository<Products, Long>, QuerydslPredicateExecutor<Products> {
    Optional<Products> findByProductId(String productId);
    void deleteByProductId(String productId);
}
