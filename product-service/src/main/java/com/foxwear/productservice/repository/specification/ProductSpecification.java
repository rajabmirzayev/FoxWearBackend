package com.foxwear.productservice.repository.specification;

import com.foxwear.common.enums.Gender;
import com.foxwear.productservice.entity.ColorOption;
import com.foxwear.productservice.entity.Product;
import com.foxwear.productservice.entity.ProductItem;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

public class ProductSpecification {

    public static Specification<Product> hasGender(List<Gender> genders) {
        return (root, query, criteriaBuilder) ->
                (genders == null || genders.isEmpty()) ? null : root.get("gender").in(genders);
    }

    public static Specification<Product> hasCategory(List<Long> categoryIds) {
        return (root, query, criteriaBuilder) ->
                (categoryIds == null || categoryIds.isEmpty()) ? null : root.get("category").get("id").in(categoryIds);
    }

    public static Specification<Product> isActive(Boolean isActive) {
        return (root, query, criteriaBuilder) ->
                isActive == null ? null : criteriaBuilder.equal(root.get("isActive"), isActive);
    }

    public static Specification<Product> isDeleted(Boolean isDeleted) {
        return (root, query, criteriaBuilder) ->
                isDeleted == null ? null : criteriaBuilder.equal(root.get("isDeleted"), isDeleted);
    }

    public static Specification<Product> hasColor(List<String> colors) {
        return (root, query, criteriaBuilder) -> {
            if (colors == null || colors.isEmpty()) return null;

            Join<Product, ColorOption> colorJoin = root.join("colors");
            if (query != null) query.distinct(true);

            return colorJoin.get("colorName").in(colors);
        };
    }

    public static Specification<Product> hasSize(List<String> sizes) {
        return (root, query, criteriaBuilder) -> {
            if (sizes == null || sizes.isEmpty()) return null;

            Join<Product, ColorOption> colors = root.join("colors");
            Join<ColorOption, ProductItem> items = colors.join("items");
            if (query != null) query.distinct(true);

            return items.get("productSize").get("sizeValue").in(sizes);
        };
    }

    public static Specification<Product> searchByTitleOrSkuOrDescription(String search) {
        return (root, query, cb) -> {
            if (search == null || search.isBlank()) return null;

            String pattern = "%" + search.toLowerCase() + "%";

            Predicate titlePredicate = cb.like(cb.lower(root.get("title")), pattern);
            Predicate descriptionPredicate = cb.like(cb.lower(root.get("description")), pattern);

            Join<Product, ColorOption> colors = root.join("colors", JoinType.LEFT);
            Join<ColorOption, ProductItem> items = colors.join("items", JoinType.LEFT);

            Predicate skuPredicate = cb.like(cb.lower(items.get("sku")), pattern);

            if (query != null) query.distinct(true);

            return cb.or(titlePredicate, descriptionPredicate, skuPredicate);
        };
    }

    public static Specification<Product> hasPriceBetween(java.math.BigInteger minPrice, java.math.BigInteger maxPrice) {
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
