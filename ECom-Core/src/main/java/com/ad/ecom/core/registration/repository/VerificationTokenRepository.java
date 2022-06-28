package com.ad.ecom.core.registration.repository;

import com.ad.ecom.core.ecomuser.persistance.EcomUser;
import com.ad.ecom.core.registration.persistance.VerificationToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface VerificationTokenRepository extends JpaRepository<VerificationToken, Long> {
    Optional<VerificationToken> findByToken(String token);
    Optional<VerificationToken> findByUser(EcomUser user);

    @Query(value = "SELECT t FROM ACTIVATION_TOKEN t WHERE t.expiresOn <= :currDate")
    Optional<List<VerificationToken>> getExpiredTokens(Date currDate);
}
