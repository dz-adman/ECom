package com.ad.ecom.orders.persistance;

import com.ad.ecom.common.dto.Item;
import com.ad.ecom.orders.stubs.OrderStatus;
import com.ad.ecom.user.persistance.Address;
import lombok.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Getter
@Setter
@ToString
@NoArgsConstructor
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.TRANSACTIONAL)
@Entity(name = "ECOM_USER_ORDERS")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter(AccessLevel.NONE)
    private long orderId;

    @Column(nullable = false)
    private long userId;

    @Column(nullable = false)
    private OrderStatus status;

    @Column(nullable = false)
    @Embedded
    private List<Item> items;

    private double subTotal;

    private double total;

    private double refundAmount;

    private String discountCodes;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "address_id", nullable = false)
    private Address deliveryAddress;

    private Boolean paid = false;

    private Boolean cancelled = false;

    private Boolean refundable = false;

    private Boolean refunded = false;

    @Column(nullable = false)
    private Date initDate;

    private Date completionDate;

    public Order(long userId, OrderStatus status, List<Item> items, double subTotal, double total, double refundAmount, List<Long> discountIDs, Address deliveryAddress, Boolean paid, Boolean cancelled, Boolean refundable, Boolean refunded, Date initDate, Date completionDate) {
        this.userId = userId;
        this.status = status;
        this.items = items;
        this.subTotal = subTotal;
        this.total = total;
        this.refundAmount = refundAmount;
        this.discountCodes = discountCodes;
        this.deliveryAddress = deliveryAddress;
        this.paid = paid;
        this.cancelled = cancelled;
        this.refundable = refundable;
        this.refunded = refunded;
        this.initDate = initDate;
        this.completionDate = completionDate;
    }

    public List<String> getDiscountCodes() {
        return new ArrayList(Arrays.asList(this.discountCodes.split(",")));
    }

    public void setDiscountCodes(List<String> discountCodes) {
        this.discountCodes = String.join(",", discountCodes);
    }

}
