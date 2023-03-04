package com.ad.ecom.registration.persistance;

import com.ad.ecom.ecomuser.persistance.EComUser;
import lombok.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Calendar;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.TRANSACTIONAL)
@Entity(name = "VERIFICATION_TOKEN")
public class VerificationToken {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Setter(AccessLevel.NONE)
    private Long id;

    @Column(nullable = false, unique = true)
    private String token;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user", nullable = false)
    private EComUser user;

    @Column(nullable = false)
    private Timestamp generatedOn;

    @Column(nullable = false)
    private Timestamp expiresOn;

    @Column(nullable = false)
    private boolean used = false;

    @Builder
    public Timestamp generateExpiryDate(int expiryTimeInMinutes) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Timestamp(cal.getTime().getTime()));
        cal.add(Calendar.MINUTE, expiryTimeInMinutes);
        return new Timestamp(cal.getTime().getTime());
    }

}
