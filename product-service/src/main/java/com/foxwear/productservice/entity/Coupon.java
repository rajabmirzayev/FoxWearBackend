package com.foxwear.productservice.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.foxwear.common.entity.BaseAuditEntity;
import com.foxwear.productservice.enums.DiscountType;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.Check;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "coupons")
@Check(name = "check_used_count_limit", constraints = "used_count <= usage_limit")
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
