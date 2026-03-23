package com.foxwear.productservice.repository;

import com.foxwear.productservice.entity.ProductSize;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductSizeRepository extends JpaRepository<ProductSize, Long> {

    boolean existsBySizeValueAndIdNot(String sizeValue, Long id);

    boolean existsBySizeValue(String sizeValue);

}
