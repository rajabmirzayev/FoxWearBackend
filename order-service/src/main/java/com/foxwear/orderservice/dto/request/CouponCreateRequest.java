package com.foxwear.orderservice.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.foxwear.orderservice.enums.DiscountType;
import jakarta.validation.constraints.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CouponCreateRequest {

    @NotBlank
    @Size(min = 10, max = 100)
    String title;

    @NotBlank
    @Size(min = 5, max = 20)
    String code;

    @NotNull
    DiscountType discountType;

    @NotNull
    @DecimalMin(value = "0.0", inclusive = false)
    BigDecimal value;

    @NotNull
    @DecimalMin("0.0")
    BigDecimal minOrderAmount;

    @JsonFormat(pattern = "dd-MM-yyyy")
    LocalDate expiryDate;

    @NotNull
    @Min(1)
    Integer usageLimit;

}
