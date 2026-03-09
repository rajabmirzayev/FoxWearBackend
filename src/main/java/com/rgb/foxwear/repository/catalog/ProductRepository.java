package com.rgb.foxwear.repository.catalog;

import com.rgb.foxwear.entity.catalog.Product;
import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<@NonNull Product, @NonNull Long> {
    Optional<Product> findByTitle(String title);
}
