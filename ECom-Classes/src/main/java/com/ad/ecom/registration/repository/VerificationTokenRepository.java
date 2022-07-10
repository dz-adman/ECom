package com.ad.ecom.registration.repository;

import com.ad.ecom.ecomuser.persistance.EcomUser;
import com.ad.ecom.registration.persistance.VerificationToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface VerificationTokenRepository extends JpaRepository<VerificationToken, Long> {
    Optional<VerificationToken> findByToken(String token);
    Optional<VerificationToken> findByUser(EcomUser user);

    Optional<List<VerificationToken>> findByExpiresOnLessThanEqual(Date currDate);
}
