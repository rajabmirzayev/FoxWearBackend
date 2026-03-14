package com.rgb.foxwear.repository.catalog.specification;

import com.rgb.foxwear.entity.catalog.ColorOption;
import com.rgb.foxwear.entity.catalog.Product;
import com.rgb.foxwear.entity.catalog.ProductItem;
import com.rgb.foxwear.enums.Gender;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import lombok.NonNull;
import org.springframework.data.jpa.domain.Specification;

public class ProductSpecification {

    public static Specification<@NonNull Product> hasGender(Gender gender) {
        return (root, query, criteriaBuilder) ->
                gender == null ? null : criteriaBuilder.equal(root.get("gender"), gender);
    }

    public static Specification<@NonNull Product> hasCategory(Long categoryId) {
        return (root, query, criteriaBuilder) ->
                categoryId == null ? null : criteriaBuilder.equal(root.get("category").get("id"), categoryId);
    }

    public static Specification<@NonNull Product> isActive(Boolean isActive) {
        return (root, query, criteriaBuilder) ->
                isActive == null ? null : criteriaBuilder.equal(root.get("isActive"), isActive);
    }

    public static Specification<@NonNull Product> isDeleted(Boolean isDeleted) {
        return (root, query, criteriaBuilder) ->
                isDeleted == null ? null : criteriaBuilder.equal(root.get("isDeleted"), isDeleted);
    }

    public static Specification<@NonNull Product> hasColor(String color) {
        return (root, query, criteriaBuilder) -> {
            if (color == null || color.isBlank()) return null;

            String searchedColor = "%" + color.toLowerCase() + "%";

            Join<Product, ColorOption> colors = root.join("colors", JoinType.LEFT);

            return criteriaBuilder.like(criteriaBuilder.lower(colors.get("colorName")), searchedColor);
        };
    }

    public static Specification<@NonNull Product> hasSize(String size) {
        return (root, query, criteriaBuilder) -> {
            if (size == null || size.isBlank()) return null;

            String searchedSize = "%" + size.toLowerCase() + "%";

            Join<Product, ColorOption> colors = root.join("colors", JoinType.LEFT);
            Join<ColorOption, ProductItem> items = colors.join("items", JoinType.LEFT);

            return criteriaBuilder.like(criteriaBuilder.lower(items.get("productSize").get("sizeValue")), searchedSize);
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
