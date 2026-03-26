package com.foxwear.interactionservice.entity;

import com.foxwear.common.entity.BaseAuditEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@MappedSuperclass
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AbstractReview extends BaseAuditEntity {

    @NotNull
    @Min(1)
    @Max(5)
    @Column(nullable = false)
    Integer rate;

    @NotBlank
    @Column(columnDefinition = "TEXT", nullable = false)
    String description;

    @NotNull
    @Column(name = "user_id", nullable = false)
    Long userId;

    @Column(name = "is_active", nullable = false)
    boolean isActive = true;

}