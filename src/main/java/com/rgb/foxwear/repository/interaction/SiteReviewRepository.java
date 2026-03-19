package com.rgb.foxwear.repository.interaction;

import com.rgb.foxwear.entity.interaction.SiteReview;
import lombok.NonNull;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SiteReviewRepository extends JpaRepository<@NonNull SiteReview, @NonNull Long> {

    @Query(
            "SELECT sr FROM SiteReview sr " +
            "JOIN FETCH sr.user " +
            "WHERE sr.isActive = true ORDER BY sr.updatedAt DESC"
    )
    List<SiteReview> findAllFirst10(Pageable pageable);

}
