package com.ad.ecom.orders.persistance;

import com.ad.ecom.discounts.repository.DiscountSubscriptionRepository;
import com.ad.ecom.orders.stubs.OrderStatus;
import com.ad.ecom.products.persistance.Product;
import com.ad.ecom.products.repository.ProductRepository;
import com.ad.ecom.user.profile.persistance.Address;
import lombok.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.TRANSACTIONAL)
@Entity(name = "ECOM_USER_ORDER")
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
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<OrderItem> items;

    private double subTotal;

    private double total;

    private double refundableAmount;

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

    private Date cancellationDate;

    @Column(nullable = false)
    private String orderStages;

    @Builder
    public Order(long userId, OrderStatus status, List<OrderItem> items, double subTotal, double total, double refundableAmount, Address deliveryAddress, Boolean paid, Boolean cancelled, Boolean refundable, Boolean refunded, Date initDate, Date completionDate) {
        this.userId = userId;
        this.status = status;
        this.items = items;
        this.subTotal = subTotal;
        this.total = total;
        this.refundableAmount = refundableAmount;
        this.deliveryAddress = deliveryAddress;
        this.paid = paid;
        this.cancelled = cancelled;
        this.refundable = refundable;
        this.refunded = refunded;
        this.initDate = initDate;
        this.completionDate = completionDate;
    }

    public List<String> getDiscountCodesList() {
        return new ArrayList(Arrays.asList(this.discountCodes.split(",")));
    }

    public void setDiscountCodes(List<String> discountCodes) {
        this.discountCodes = String.join(",", discountCodes);
    }

    public List<OrderStatus> getOrderStagesList() {
        List<String> stages = new ArrayList(Arrays.asList(this.orderStages.split(",")));
        return stages.stream().map(s -> OrderStatus.valueOf(s)).collect(Collectors.toList());
    }

    public void setOrderStages(List<OrderStatus> orderStages) {
        List<String> stages = orderStages.stream().map(s -> s.name()).collect(Collectors.toList());
        this.orderStages = String.join(",", stages);
    }

    public void addOrderStages(List<OrderStatus> list) {
        var stages = this.getOrderStagesList();
        list.stream().forEach(item -> stages.add(item));
        this.setOrderStages(stages);
    }

    public void calculateAndSetRefundableAmountAndFlag(ProductRepository productsRepo, DiscountSubscriptionRepository discountSubsRepo) {
        List<Optional<Product>> products = this.getItems().stream().map(i -> productsRepo.findByProductId(i.getItemProductId())).collect(Collectors.toList());
        double refundAmount = 0.0;
        for(Optional<Product> p : products) {
            if(p.isEmpty()) continue;
            if(p.get().isRefundable()) {
                double effectivePrice = p.get().getPrice() - p.get().getDiscountOnProduct(discountSubsRepo);
                refundAmount += effectivePrice * (p.get().getRefundPercentage() / 100);
            }
        }
        if(refundAmount > 0.0)  this.setRefundableAmount(refundAmount);
    }
}
