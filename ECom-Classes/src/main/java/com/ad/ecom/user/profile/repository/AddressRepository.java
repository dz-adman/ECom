package com.ad.ecom.user.profile.repository;

import com.ad.ecom.user.profile.persistance.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AddressRepository extends JpaRepository<Address, Long> {
    Optional<List<Address>> findAllByUserId(long userId);
    Optional<Address> findByUserIdAndDefaultAddressTrue(long userId);
    Optional<Address> findByUserIdAndId(long userId, long addressId);
}
