package com.ad.ecom.discounts.persistance;

import com.ad.ecom.discounts.stubs.DiscountStatus;
import com.ad.ecom.products.persistance.Product;
import lombok.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.TRANSACTIONAL)
@Entity(name = "ECOM_PRDCT_DISCNT_SUBS")
@Table(uniqueConstraints = {@UniqueConstraint(columnNames = {"id", "discount_id", "product_id"})})
public class DiscountSubscription {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter(AccessLevel.NONE)
    private long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "discount_id", nullable = false)
    private Discount discount;

    @Column(nullable = false)
    private DiscountStatus status;

    @Builder
    public DiscountSubscription(Discount discount, Product product, DiscountStatus status) {
        this.discount = discount;
        this.product = product;
        this.status = status;
    }
}
