package com.ad.ecom.orders.persistance;

import com.ad.ecom.products.stubs.ProductStockUnit;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity(name = "ECOM_ORDER_ITEMS")
public class OrderItems {

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
}
