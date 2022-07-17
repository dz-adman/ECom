package com.ad.ecom.discounts.repository;

import com.ad.ecom.discounts.persistence.Discount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DiscountRepository extends JpaRepository<Discount, Long>, PagingAndSortingRepository<Discount, Long>, QuerydslPredicateExecutor<Discount> {
    public Discount findByCode(String code);
    public void deleteByCode(long code);
}
