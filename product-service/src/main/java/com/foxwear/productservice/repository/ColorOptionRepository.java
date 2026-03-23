package com.foxwear.productservice.repository;

import com.foxwear.productservice.entity.ColorOption;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ColorOptionRepository extends JpaRepository<ColorOption, Long> {

    @Query("SELECT c FROM ColorOption c WHERE c.id IN " +
            "(SELECT MIN(sub.id) FROM ColorOption sub GROUP BY sub.colorName)")
    List<ColorOption> findAllUniqueColorNames();

}
