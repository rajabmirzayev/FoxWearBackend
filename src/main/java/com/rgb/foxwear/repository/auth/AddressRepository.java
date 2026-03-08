package com.rgb.foxwear.repository.auth;

import com.rgb.foxwear.entity.auth.Address;
import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AddressRepository extends JpaRepository<@NonNull Address, @NonNull Long> {
}
