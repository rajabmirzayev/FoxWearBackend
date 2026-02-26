package com.rgb.foxwear.entity.interaction;

import com.rgb.foxwear.entity.BaseAuditEntity;
import com.rgb.foxwear.entity.auth.UserEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
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
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    UserEntity user;

    @Column(name = "is_active", nullable = false)
    boolean isActive = true;

}