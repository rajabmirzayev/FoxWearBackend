package com.rgb.foxwear.repository.interaction;

import com.rgb.foxwear.entity.auth.UserEntity;
import com.rgb.foxwear.entity.catalog.Product;
import com.rgb.foxwear.entity.interaction.ProductLike;
import lombok.NonNull;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface ProductLikeRepository extends JpaRepository<@NonNull ProductLike, @NonNull Long> {

    @Query("SELECT pl.product FROM ProductLike pl " +
            "GROUP BY pl.product " +
            "ORDER BY COUNT(pl.id) DESC")
    List<Product> findTopMostLikedProducts(Pageable pageable);

    @EntityGraph(attributePaths = {"user", "product"})
    Optional<ProductLike> findByUserAndProduct(UserEntity user, Product product);

    @Query("SELECT pl.product.id FROM ProductLike pl WHERE pl.user.username = :username")
    Set<Long> findAllLikedProductIdsByUsername(@Param("username") String username);

    boolean existsByUserUsernameAndProduct(String userUsername, Product product);
}
