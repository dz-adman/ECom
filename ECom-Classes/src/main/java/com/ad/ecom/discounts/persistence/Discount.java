package com.ad.ecom.discounts.persistence;

import com.ad.ecom.discounts.stubs.DiscountStatus;
import com.ad.ecom.util.SequenceGenerator;
import lombok.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import javax.persistence.*;
import java.sql.Date;

@Getter
@Setter
@NoArgsConstructor
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.TRANSACTIONAL)
@Entity(name = "ECOM_PRDCT_DISCNT")
public class Discount {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter(AccessLevel.NONE)
    private long id;

    @GenericGenerator(name = "discountCode_seq", strategy = "com.ad.ecom.core.util.SeqGenerator",
            parameters = {
                @Parameter(name = SequenceGenerator.INCREMENT_PARAM, value = "50"),
                @Parameter(name = SequenceGenerator.VALUE_PREFIX_PARAMETER, value = "DISCNT_"),
                @Parameter(name = SequenceGenerator.NUMBER_FORMAT_PARAMETER, value = "%05d")})
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "discountCode_seq")
    @Column(nullable = false, unique = true)
    private String code;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private Date validFrom;

    @Column(nullable = false)
    private Date validTo;

    @Column(nullable = false)
    private double percentageValue;

    @Column(nullable = false)
    private DiscountStatus status;

    @Builder
    public Discount(String name, Date validFrom, Date validTo, double percentageValue, DiscountStatus status) {
        this.name = name;
        this.validFrom = validFrom;
        this.validTo = validTo;
        this.percentageValue = percentageValue;
        this.status = status;
    }
}
