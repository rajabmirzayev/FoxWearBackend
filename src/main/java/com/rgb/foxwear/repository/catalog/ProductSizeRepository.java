package com.rgb.foxwear.repository.catalog;

import com.rgb.foxwear.entity.catalog.ProductSize;
import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProductSizeRepository extends JpaRepository<@NonNull ProductSize, @NonNull Long> {

    boolean existsBySizeValueAndIdNot(String sizeValue, Long id);

    boolean existsBySizeValue(String sizeValue);
}
