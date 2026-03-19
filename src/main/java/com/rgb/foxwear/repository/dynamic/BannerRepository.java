package com.rgb.foxwear.repository.dynamic;

import com.rgb.foxwear.entity.dynamic.Banner;
import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BannerRepository extends JpaRepository<@NonNull Banner, @NonNull Long> {
    Optional<Banner> findByPlacement(String placement);

    boolean existsByPlacement(String placement);

    boolean existsByPlacementAndIdNot(String placement, Long id);

    Optional<Banner> findByPlacementAndActive(String placement, boolean active);
}
