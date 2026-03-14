package com.rgb.foxwear.repository.catalog;

import com.rgb.foxwear.entity.catalog.Product;
import lombok.NonNull;
import org.jspecify.annotations.NullMarked;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<@NonNull Product, @NonNull Long>, JpaSpecificationExecutor<@NonNull Product> {
    @Override
    @NullMarked
    @EntityGraph(attributePaths = {"category"})
    Page<Product> findAll(Specification<Product> spec, Pageable pageable);

    @NullMarked
    @EntityGraph(attributePaths = {"category"})
    Optional<Product> findById(Long id);

}
