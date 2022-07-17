package com.ad.ecom.products.persistence;

import com.ad.ecom.common.EComDate;
import com.ad.ecom.discounts.persistence.DiscountSubscription;
import com.ad.ecom.discounts.repository.DiscountSubscriptionRepository;
import com.ad.ecom.discounts.stubs.DiscountStatus;
import com.ad.ecom.products.stubs.*;
import com.ad.ecom.util.DateConverter;
import com.ad.ecom.util.SequenceGenerator;
import lombok.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.sql.Date;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.TRANSACTIONAL)
@Entity(name = "ECOM_PRODUCT")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter(AccessLevel.NONE)
    private long id;

    @GenericGenerator(name = "productId_seq", strategy = "com.ad.ecom.core.util.SeqGenerator",
            parameters = {
                    @org.hibernate.annotations.Parameter(name = SequenceGenerator.INCREMENT_PARAM, value = "50"),
                    @org.hibernate.annotations.Parameter(name = SequenceGenerator.VALUE_PREFIX_PARAMETER, value = "PRDCT_"),
                    @org.hibernate.annotations.Parameter(name = SequenceGenerator.NUMBER_FORMAT_PARAMETER, value = "%05d")})
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "productId_seq")
    @Column(nullable = false, unique = true)
    private String productId;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private ProductCategory category;

    @Column(nullable = false)
    private ProductSubCategory subCategory;

    @Column(nullable = false)
    private ProductBrand brand;

    @Column(nullable = false)
    private double price;

    @Column(nullable = false)
    private long stock;

    @Column(nullable = false)
    private ProductStockUnit stockUnit;

    @Column(nullable = false)
    private boolean refundable;

    private Double refundPercentage;

    @Column(nullable = false)
    private long productOwnerId;

    @Column(nullable = false)
    private ProductStatus status;

    @Builder
    public Product(String name, ProductCategory category, ProductSubCategory subCategory, ProductBrand brand, double price, long stock, ProductStockUnit stockUnit, long productOwnerId, ProductStatus status, boolean refundable) {
        this.name = name;
        this.category = category;
        this.subCategory = subCategory;
        this.brand = brand;
        this.price = price;
        this.stock = stock;
        this.stockUnit = stockUnit;
        this.productOwnerId = productOwnerId;
        this.status = status;
        this.refundable = refundable;
    }

    public double getDiscountOnProduct(DiscountSubscriptionRepository discountSubsRepo) {
        List<DiscountSubscription> subs = this.getDiscountsApplicableOnProduct(discountSubsRepo);
        if(subs.isEmpty()) return 0.0;
        double totalDiscountPercentage = 0.0;
        for(DiscountSubscription subscription : subs)
            totalDiscountPercentage += subscription.getDiscount().getPercentageValue();
        return (totalDiscountPercentage/100.0)*this.getPrice();
    }

    public List<DiscountSubscription> getDiscountsApplicableOnProduct(DiscountSubscriptionRepository discountSubsRepo) {
        List<DiscountSubscription> subs = discountSubsRepo.findByProductId(this.getId());
        // filter and keep only active discount-subscriptions
        subs.stream().filter(sub -> sub.getStatus().equals(DiscountStatus.ACTIVE));
        if(subs.isEmpty()) return Collections.emptyList();
        for(Iterator<DiscountSubscription> itr = subs.iterator(); itr.hasNext();) {
            DiscountSubscription thisObj = itr.next();
            EComDate validFrom = DateConverter.convertToECcmDate(thisObj.getDiscount().getValidFrom());
            EComDate validUpto = DateConverter.convertToECcmDate(thisObj.getDiscount().getValidTo());
            EComDate currDate = DateConverter.convertToECcmDate(new Date(System.currentTimeMillis()));
            if(!(validFrom.ltEq(currDate) && validUpto.gtEq(currDate)))
                itr.remove();
        }
        return subs;
    }
}
