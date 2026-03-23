package com.foxwear.interactionservice.repository;

import com.foxwear.interactionservice.entity.SiteReview;
import lombok.NonNull;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SiteReviewRepository extends JpaRepository<@NonNull SiteReview, @NonNull Long> {

    @Query("SELECT sr FROM SiteReview sr ORDER BY sr.updatedAt DESC LIMIT 10")
    List<SiteReview> findAllFirst10(Pageable pageable);

}
