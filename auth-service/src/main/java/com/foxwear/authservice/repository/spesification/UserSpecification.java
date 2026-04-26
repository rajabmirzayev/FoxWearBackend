package com.foxwear.authservice.repository.spesification;

import com.foxwear.authservice.entity.UserEntity;
import com.foxwear.common.enums.Gender;
import com.foxwear.common.enums.Role;
import com.foxwear.common.enums.UserStatus;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

public class UserSpecification {

    public static Specification<UserEntity> hasGender(List<Gender> genders) {
        return ((root, query, criteriaBuilder) ->
                (genders == null || genders.isEmpty()) ? null : root.get("gender").in(genders));
    }

    public static Specification<UserEntity> hasRole(List<Role> roles) {
        return ((root, query, criteriaBuilder) ->
                (roles == null || roles.isEmpty()) ? null : root.get("role").in(roles));
    }

    public static Specification<UserEntity> hasStatus(List<UserStatus> statuses) {
        return ((root, query, criteriaBuilder) ->
                (statuses == null || statuses.isEmpty()) ? null : root.get("status").in(statuses));
    }

    public static Specification<UserEntity> isEmailVerified(Boolean isEmailVerified) {
        return ((root, query, criteriaBuilder) ->
                isEmailVerified == null ? null : criteriaBuilder.equal(root.get("isEmailVerified"), isEmailVerified));
    }

    public static Specification<UserEntity> isPhoneNumberVerified(Boolean isPhoneNumberVerified) {
        return ((root, query, criteriaBuilder) ->
                isPhoneNumberVerified == null ? null : criteriaBuilder.equal(root.get("isPhoneNumberVerified"), isPhoneNumberVerified));
    }

    public static Specification<UserEntity> isTwoFactorEnabled(Boolean twoFactorEnabled) {
        return ((root, query, criteriaBuilder) ->
                twoFactorEnabled == null ? null : criteriaBuilder.equal(root.get("twoFactorEnabled"), twoFactorEnabled));
    }

    public static Specification<UserEntity> search(String keyword) {
        return ((root, query, cb) -> {
            if (keyword == null || keyword.isBlank()) return null;

            String pattern = "%" + keyword.toLowerCase() + "%";

            Predicate namePredicate = cb.like(cb.lower(root.get("firstName")), pattern);
            Predicate surnamePredicate = cb.like(cb.lower(root.get("lastName")), pattern);
            Predicate emailPredicate = cb.like(cb.lower(root.get("email")), pattern);
            Predicate usernamePredicate = cb.like(cb.lower(root.get("username")), pattern);
            Predicate phoneNumberPredicate = cb.like(cb.lower(root.get("phoneNumber")), pattern);

            if (query != null) query.distinct(true);

            return cb.or(namePredicate, surnamePredicate, emailPredicate, usernamePredicate, phoneNumberPredicate);
        });
    }

}
