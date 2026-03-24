package com.foxwear.interactionservice.repository;

import com.foxwear.interactionservice.entity.SiteReview;
import lombok.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface SiteReviewRepository extends JpaRepository<@NonNull SiteReview, @NonNull Long> {

    @Query("SELECT s FROM SiteReview s " +
            "WHERE s.isActive = true " +
            "ORDER BY s.updatedAt DESC")
    Page<SiteReview> findAllByIsActiveTrue(Pageable pageable);

    @Query("SELECT s FROM SiteReview s " +
            "WHERE s.isActive = true AND s.userId = :userId " +
            "ORDER BY s.updatedAt DESC")
    List<SiteReview> findAllByUserId(@Param("userId") Long userId);

    @Query("SELECT AVG(s.rate) FROM SiteReview s WHERE s.isActive = true")
    Double getAverageRate();

}
