package com.rgb.foxwear.repository.catalog;

import com.rgb.foxwear.entity.catalog.ColorOption;
import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ColorOptionRepository extends JpaRepository<@NonNull ColorOption, @NonNull Long> {

    @Query("SELECT c FROM ColorOption c WHERE c.id IN " +
            "(SELECT MIN(sub.id) FROM ColorOption sub GROUP BY sub.colorName)")
    List<ColorOption> findAllUniqueColorNames();

}
