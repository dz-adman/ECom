package com.ad.ecom.user.persistance;

import com.ad.ecom.user.stubs.AddressType;
import lombok.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

@Data
@NoArgsConstructor
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.TRANSACTIONAL)
@Entity(name = "ECOM_USER_ADDRESS")
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter(AccessLevel.NONE)
    private long id;

    @Column(nullable = false)
    private long userId;

    @Column(nullable = false)
    private AddressType addressType;

    private boolean defaultAddress = false;

    @Column(nullable = false)
    private String address;

    @Column(nullable = false)
    private String city;

    @Column(nullable = false)
    private String state;

    @Column(nullable = false)
    private String country;

    @Column(nullable = false)
    private int pinCode;

    private String landmark;

    @Builder
    public Address(long userId, AddressType addressType, boolean defaultAddress, String address, String city, String state, String country, int pinCode, String landmark) {
        this.userId = userId;
        this.addressType = addressType;
        this.defaultAddress = defaultAddress;
        this.address = address;
        this.city = city;
        this.state = state;
        this.country = country;
        this.pinCode = pinCode;
        this.landmark = landmark;
    }
}
