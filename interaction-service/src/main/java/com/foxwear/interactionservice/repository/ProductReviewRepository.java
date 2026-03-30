package com.foxwear.interactionservice.repository;

import com.foxwear.interactionservice.entity.ProductReview;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductReviewRepository extends JpaRepository<ProductReview, Long> {

    @Query("SELECT p FROM ProductReview p " +
            "WHERE p.isActive = true AND p.productId = :productId " +
            "ORDER BY p.updatedAt DESC")
    Page<ProductReview> findAllByIsActiveTrue(Pageable pageable, @Param("productId") Long productId);

    @Query("SELECT p FROM ProductReview p " +
            "WHERE p.isActive = true AND p.userId = :userId " +
            "ORDER BY p.updatedAt DESC")
    List<ProductReview> findAllByUserId(@Param("userId") Long userId);

    @Query("SELECT AVG(p.rate) FROM ProductReview p WHERE p.isActive = true AND p.productId = :productId")
    Double getAverageRate(@Param("productId") Long productId);

}
