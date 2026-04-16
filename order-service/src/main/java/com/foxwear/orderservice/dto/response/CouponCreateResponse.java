package com.foxwear.orderservice.dto.response;

import com.foxwear.orderservice.enums.DiscountType;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CouponCreateResponse {

    Long id;
    String title;
    String code;
    DiscountType discountType;
    BigDecimal value;
    BigDecimal minOrderAmount;
    LocalDate expiryDate;
    Integer usageLimit;
    boolean isActive;

}
