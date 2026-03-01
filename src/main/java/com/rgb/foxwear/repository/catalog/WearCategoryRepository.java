package com.rgb.foxwear.repository.catalog;

import com.rgb.foxwear.entity.catalog.WearCategory;
import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WearCategoryRepository extends JpaRepository<@NonNull WearCategory, @NonNull Long> {
}
