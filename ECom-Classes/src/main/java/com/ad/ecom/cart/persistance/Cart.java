package com.ad.ecom.cart.persistance;

import lombok.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
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

    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL)
    private List<CartItems> items = new ArrayList<>();

    private double subTotal = 0.0;

    private Date lastUpdatedOn;

    @Builder
    public Cart(long userId, List<CartItems> items, double subTotal, Date lastUpdatedOn) {
        this.userId = userId;
        this.items = items;
        this.subTotal = subTotal;
        this.lastUpdatedOn = lastUpdatedOn;
    }
}


