package com.ad.ecom.cart.persistance;

import com.ad.ecom.common.dto.Item;
import lombok.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ToString
@NoArgsConstructor
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.TRANSACTIONAL)
@Entity(name = "ECOM_USER_CART")
public class Cart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter(AccessLevel.NONE)
    private long id;

    @Column(nullable = false)
    private long userId;

    @Embedded
    private List<Item> items = new ArrayList<>();

    private double subTotal = 0.0;

    private Date lastUpdatedOn;

    public Cart(long userId, List<Item> items, double subTotal, Date lastUpdatedOn) {
        this.userId = userId;
        this.items = items;
        this.subTotal = subTotal;
        this.lastUpdatedOn = lastUpdatedOn;
    }
}
