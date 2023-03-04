package com.ad.ecom.ecomuser.repository;

import com.ad.ecom.ecomuser.persistance.EComUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EcomUserRepository extends JpaRepository<EComUser, Long> {
    Optional<EComUser> findByLoginIdAndDeletedFalse(String loginId);
    Optional<EComUser> findByLoginIdOrEmailAndDeletedFalse(String loginId, String email);
    boolean existsByLoginIdAndEmailAndDeletedFalse(String loginId, String email);
}
