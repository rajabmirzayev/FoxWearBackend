package com.rgb.foxwear.repository.interaction;

import com.rgb.foxwear.entity.catalog.Product;
import com.rgb.foxwear.entity.interaction.ProductLike;
import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ProductLikeRepository extends JpaRepository<@NonNull ProductLike, @NonNull Long> {

    @Query("SELECT pl.product FROM ProductLike pl GROUP BY pl.product ORDER BY COUNT(pl.id) DESC LIMIT 10")
    List<Product> findTopMostLikedProducts();

}
