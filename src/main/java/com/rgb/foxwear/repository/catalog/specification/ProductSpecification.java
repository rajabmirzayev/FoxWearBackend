package com.rgb.foxwear.repository.catalog.specification;

import com.rgb.foxwear.entity.catalog.ColorOption;
import com.rgb.foxwear.entity.catalog.Product;
import com.rgb.foxwear.entity.catalog.ProductItem;
import com.rgb.foxwear.enums.Gender;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import java.util.List;
import lombok.NonNull;
import org.springframework.data.jpa.domain.Specification;

public class ProductSpecification {

    public static Specification<@NonNull Product> hasGender(List<Gender> genders) {
        return (root, query, criteriaBuilder) ->
                (genders == null || genders.isEmpty()) ? null : root.get("gender").in(genders);
    }

    public static Specification<@NonNull Product> hasCategory(List<Long> categoryIds) {
        return (root, query, criteriaBuilder) ->
                (categoryIds == null || categoryIds.isEmpty()) ? null : root.get("category").get("id").in(categoryIds);
    }

    public static Specification<@NonNull Product> isActive(Boolean isActive) {
        return (root, query, criteriaBuilder) ->
                isActive == null ? null : criteriaBuilder.equal(root.get("isActive"), isActive);
    }

    public static Specification<@NonNull Product> isDeleted(Boolean isDeleted) {
        return (root, query, criteriaBuilder) ->
                isDeleted == null ? null : criteriaBuilder.equal(root.get("isDeleted"), isDeleted);
    }

    public static Specification<@NonNull Product> hasColor(List<String> colors) {
        return (root, query, criteriaBuilder) -> {
            if (colors == null || colors.isEmpty()) return null;

            Join<Product, ColorOption> colorJoin = root.join("colors");
            query.distinct(true);

            return colorJoin.get("colorName").in(colors);
        };
    }

    public static Specification<@NonNull Product> hasSize(List<String> sizes) {
        return (root, query, criteriaBuilder) -> {
            if (sizes == null || sizes.isEmpty()) return null;

            Join<Product, ColorOption> colors = root.join("colors");
            Join<ColorOption, ProductItem> items = colors.join("items");
            query.distinct(true);

            return items.get("productSize").get("sizeValue").in(sizes);
        };
    }

    public static Specification<@NonNull Product> searchByTitleOrSkuOrDescription(String search) {
        return (root, query, cb) -> {
            if (search == null || search.isBlank()) return null;

            String pattern = "%" + search.toLowerCase() + "%";

            Predicate titlePredicate = cb.like(cb.lower(root.get("title")), pattern);
            Predicate descriptionPredicate = cb.like(cb.lower(root.get("description")), pattern);

            Join<Product, ColorOption> colors = root.join("colors", JoinType.LEFT);
            Join<ColorOption, ProductItem> items = colors.join("items", JoinType.LEFT);

            Predicate skuPredicate = cb.like(cb.lower(items.get("sku")), pattern);

            query.distinct(true);

            return cb.or(titlePredicate, descriptionPredicate, skuPredicate);
        };
    }

    public static Specification<@NonNull Product> hasPriceBetween(java.math.BigInteger minPrice, java.math.BigInteger maxPrice) {
        return (root, query, cb) -> {
            Predicate predicate = cb.conjunction();

            if (minPrice != null) {
                predicate = cb.and(predicate, cb.greaterThanOrEqualTo(root.get("discountPrice"), minPrice));
            }

            if (maxPrice != null) {
                predicate = cb.and(predicate, cb.lessThanOrEqualTo(root.get("discountPrice"), maxPrice));
            }

            return predicate;
        };
    }

}
