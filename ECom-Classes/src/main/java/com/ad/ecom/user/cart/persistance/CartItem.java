package com.ad.ecom.user.cart.persistance;

import com.ad.ecom.products.stubs.ProductStockUnit;
import lombok.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.TRANSACTIONAL)
@Entity(name = "ECOM_CART_ITEM")
public class CartItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter(AccessLevel.NONE)
    private long id;
    @ManyToOne(fetch = FetchType.LAZY)
    private Cart cart;
    @Column(nullable = false)
    private String itemProductId;
    private String itemProductName;
    @Column(nullable = false)
    private long itemQuantity;
    @Column(nullable = false)
    private ProductStockUnit itemUnit;

    @Builder
    public CartItem(Cart cart, String itemProductId, String itemProductName, long itemQuantity, ProductStockUnit itemUnit) {
        this.cart = cart;
        this.itemProductId = itemProductId;
        this.itemProductName = itemProductName;
        this.itemQuantity = itemQuantity;
        this.itemUnit = itemUnit;
    }
}