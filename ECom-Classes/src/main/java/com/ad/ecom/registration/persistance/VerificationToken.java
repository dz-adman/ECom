package com.ad.ecom.registration.persistance;

import com.ad.ecom.ecomuser.persistance.EcomUser;
import lombok.*;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Calendar;

@Data
@NoArgsConstructor
@AllArgsConstructor
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
    private EcomUser user;

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
