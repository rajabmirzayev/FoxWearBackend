package com.rgb.foxwear.repository.catalog.specification;

import com.rgb.foxwear.entity.catalog.Product;
import com.rgb.foxwear.enums.Gender;
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

    public static Specification<@NonNull Product> hasKeyword(String keyword) {
        return (root, query, criteriaBuilder) ->
                keyword == null ? null : criteriaBuilder.like(
                        criteriaBuilder.lower(root.get("title")),
                        "%" + keyword.toLowerCase() + "%"
                );
    }

    public static Specification<@NonNull Product> isDeleted(Boolean isDeleted) {
        return (root, query, criteriaBuilder) ->
                isDeleted == null ? null : criteriaBuilder.equal(root.get("isDeleted"), isDeleted);
    }

}
