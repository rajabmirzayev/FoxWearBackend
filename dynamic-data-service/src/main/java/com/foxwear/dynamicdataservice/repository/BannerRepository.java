package com.foxwear.dynamicdataservice.repository;

import com.foxwear.dynamicdataservice.entity.Banner;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BannerRepository extends JpaRepository<Banner, Long> {

    Optional<Banner> findByPlacement(String placement);

    boolean existsByPlacement(String placement);

    boolean existsByPlacementAndIdNot(String placement, Long id);

    Optional<Banner> findByPlacementAndActive(String placement, boolean active);

}
