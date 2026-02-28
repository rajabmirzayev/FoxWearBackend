package com.rgb.foxwear.repository;

import com.rgb.foxwear.entity.auth.Address;
import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AddressRepository extends JpaRepository<@NonNull Address, @NonNull Long> {
}
