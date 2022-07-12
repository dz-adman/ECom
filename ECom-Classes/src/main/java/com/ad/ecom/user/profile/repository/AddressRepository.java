package com.ad.ecom.user.profile.repository;

import com.ad.ecom.user.profile.persistance.Address;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AddressRepository extends JpaRepository<Address, Long> {
    Optional<List<Address>> findAllByUserId(long userId);
    Optional<Address> findByUserIdAndDefaultAddressTrue(long userId);
}
