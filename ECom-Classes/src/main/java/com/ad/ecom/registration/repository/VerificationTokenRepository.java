package com.ad.ecom.registration.repository;

import com.ad.ecom.ecomuser.persistance.EComUser;
import com.ad.ecom.registration.persistance.VerificationToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface VerificationTokenRepository extends JpaRepository<VerificationToken, Long> {
    Optional<VerificationToken> findByToken(String token);
    Optional<VerificationToken> findByUser(EComUser user);

    Optional<List<VerificationToken>> findByExpiresOnLessThanEqual(Date currDate);
}
