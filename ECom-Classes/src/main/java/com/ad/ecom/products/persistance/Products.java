package com.ad.ecom.products.persistance;

import com.ad.ecom.common.stub.EComDate;
import com.ad.ecom.discounts.persistance.DiscountSubscription;
import com.ad.ecom.discounts.repository.DiscountSubscriptionsRepository;
import com.ad.ecom.products.stubs.*;
import com.ad.ecom.util.DateConverter;
import com.ad.ecom.util.SequenceGenerator;
import lombok.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.sql.Date;
import java.util.List;

@Getter
@Setter
@ToString
@NoArgsConstructor
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.TRANSACTIONAL)
@Entity(name = "ECOM_PRODUCTS")
public class Products {

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
    private long productOwnerId;

    @Column(nullable = false)
    private ProductStatus status;

    public Products(String name, ProductCategory category, ProductSubCategory subCategory, ProductBrand brand, double price, long stock, ProductStockUnit stockUnit, long productOwnerId, ProductStatus status) {
        this.name = name;
        this.category = category;
        this.subCategory = subCategory;
        this.brand = brand;
        this.price = price;
        this.stock = stock;
        this.stockUnit = stockUnit;
        this.productOwnerId = productOwnerId;
        this.status = status;
    }

    public double getDiscountOnProduct(DiscountSubscriptionsRepository discountSubsRepo) {
        List<DiscountSubscription> subs = discountSubsRepo.findByProductId(this.getId());
        int totalDiscountPercentage = 0;
        for(DiscountSubscription subscription : subs) {
            EComDate validFrom = DateConverter.convertToECcmDate(subscription.getDiscount().getValidFrom());
            EComDate validUpto = DateConverter.convertToECcmDate(subscription.getDiscount().getValidTo());
            EComDate currDate = DateConverter.convertToECcmDate(new Date(System.currentTimeMillis()));
            if(validFrom.ltEq(currDate) && validUpto.gtEq(currDate))
                totalDiscountPercentage += subscription.getDiscount().getPercentageValue();
        }
        return (totalDiscountPercentage/100.0)*this.getPrice();
    }
}
