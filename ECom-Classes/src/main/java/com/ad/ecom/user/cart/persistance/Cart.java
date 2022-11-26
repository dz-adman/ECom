package com.ad.ecom.user.cart.persistance;

import lombok.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.TRANSACTIONAL)
@Entity(name = "ECOM_USER_CART")
@Table(uniqueConstraints = {@UniqueConstraint(columnNames = {"id", "userId"})})
public class Cart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter(AccessLevel.NONE)
    private long id;

    @Column(nullable = false)
    private long userId;

    private long deliveryAddressId;

    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CartItem> items = new ArrayList<>();

    @Builder
    public Cart(long userId, List<CartItem> items, long deliveryAddressId) {
        this.userId = userId;
        this.items = items;
        this.deliveryAddressId = deliveryAddressId;
    }
}


