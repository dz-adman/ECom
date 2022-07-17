package com.ad.ecom.orders.persistance;

import com.ad.ecom.products.stubs.ProductStockUnit;
import lombok.*;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.TRANSACTIONAL)
@Entity(name = "ECOM_ORDER_ITEM")
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter(AccessLevel.NONE)
    private long id;
    @ManyToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Order order;
    @Column(nullable = false)
    private String itemProductId;
    private String itemProductName;
    @Column(nullable = false)
    private long itemQuantity;
    @Column(nullable = false)
    private ProductStockUnit itemUnit;

    @Builder
    public OrderItem(Order order, String itemProductId, String itemProductName, long itemQuantity, ProductStockUnit itemUnit) {
        this.order = order;
        this.itemProductId = itemProductId;
        this.itemProductName = itemProductName;
        this.itemQuantity = itemQuantity;
        this.itemUnit = itemUnit;
    }
}
