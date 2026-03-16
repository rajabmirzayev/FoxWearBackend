package com.rgb.foxwear.repository.catalog;

import com.rgb.foxwear.entity.catalog.Product;
import com.rgb.foxwear.entity.catalog.ProductItem;
import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface ProductItemRepository extends JpaRepository<@NonNull ProductItem, @NonNull Long> {
//    List<Product>
}
