package com.foxwear.productservice.repository;

import com.foxwear.productservice.entity.ProductItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface ProductItemRepository extends JpaRepository<ProductItem, Long> {
}
