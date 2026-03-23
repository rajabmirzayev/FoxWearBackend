package com.foxwear.productservice.repository;

import com.foxwear.productservice.entity.Product;
import org.jspecify.annotations.NullMarked;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long>, JpaSpecificationExecutor<Product> {

    @Override
    @NullMarked
    @EntityGraph(attributePaths = {"category"})
    Page<Product> findAll(Specification<Product> spec, Pageable pageable);

    @NullMarked
    @EntityGraph(attributePaths = {"category"})
    Optional<Product> findById(Long id);

    @NullMarked
    @EntityGraph(attributePaths = {"category"})
    Optional<Product> findBySlug(String slug);

    @Query("SELECT p FROM Product p WHERE p.isActive = true AND p.isDeleted = false ORDER BY p.likedCount DESC LIMIT 10")
    List<Product> findTop10MostLiked();

}