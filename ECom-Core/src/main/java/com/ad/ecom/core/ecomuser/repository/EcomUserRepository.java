package com.ad.ecom.core.ecomuser.repository;

import com.ad.ecom.core.ecomuser.persistance.EcomUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EcomUserRepository extends JpaRepository<EcomUser, Long> {
    Optional<EcomUser> findByLoginIdAndDeletedFalse(String loginId);
    Optional<EcomUser> findByLoginIdOrEmailAndDeletedFalse(String loginId, String email);
    boolean existsByLoginIdAndEmailAndDeletedFalse(String loginId, String email);
}
