package com.ad.ecom.discounts.repository;

import com.ad.ecom.discounts.persistance.Discounts;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DiscountsRepository extends JpaRepository<Discounts, Long>, PagingAndSortingRepository<Discounts, Long>, QuerydslPredicateExecutor<Discounts> {
    public Discounts findByCode(String code);
    public void deleteByCode(long code);
}
