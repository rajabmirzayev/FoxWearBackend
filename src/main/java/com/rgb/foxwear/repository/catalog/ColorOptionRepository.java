package com.rgb.foxwear.repository.catalog;

import com.rgb.foxwear.entity.catalog.ColorOption;
import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ColorOptionRepository extends JpaRepository<@NonNull ColorOption, @NonNull Long> {
}
