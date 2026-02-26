package com.rgb.foxwear.entity.catalog;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.rgb.foxwear.entity.BaseAuditEntity;
import com.rgb.foxwear.enums.DiscountType;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(
        name = "coupons",
        schema = "catalog",
        check = {
                @CheckConstraint(name = "check_used_count_limit", constraint = "used_count <= usage_limit")
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Coupon extends BaseAuditEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @NotBlank
    @Size(min = 10, max = 100)
    @Column(nullable = false, length = 100)
    String title;

    @NotBlank
    @Size(min = 5, max = 20)
    @Column(nullable = false, unique = true, length = 20)
    String code;

    @NotNull
    @Enumerated(EnumType.STRING)
    DiscountType discountType;

    @NotNull
    @DecimalMin(value = "0.0", inclusive = false)
    @Column(nullable = false, precision = 10, scale = 2)
    BigDecimal value;

    @NotNull
    @DecimalMin("0.0")
    @Column(name = "min_order_amount", nullable = false, precision = 10, scale = 2)
    BigDecimal minOrderAmount;

    @JsonFormat(pattern = "dd-MM-yyyy")
    @Column(name = "expiry_date")
    LocalDate expiryDate;

    @NotNull
    @Min(1)
    @Column(name = "usage_limit", nullable = false)
    Integer usageLimit;

    @Column(name = "used_count")
    int usedCount = 0;

    @Column(name = "is_active", nullable = false)
    boolean isActive = true;

}
