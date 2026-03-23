package com.foxwear.interactionservice.repository;

import com.foxwear.interactionservice.entity.ProductLike;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface ProductLikeRepository extends JpaRepository<ProductLike, Long> {

    boolean existsByProductIdAndUserId(Long productId, Long userId);

    void deleteByProductIdAndUserId(Long productId, Long userId);

    @Query("SELECT pl.productId FROM ProductLike pl WHERE pl.userId = :userId")
    Set<Long> findAllProductIdsByUserId(@Param("userId") Long userId);

}