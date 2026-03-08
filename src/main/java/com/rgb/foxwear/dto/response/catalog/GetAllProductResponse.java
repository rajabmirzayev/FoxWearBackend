package com.rgb.foxwear.dto.response.catalog;

import com.rgb.foxwear.enums.Gender;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class GetAllProductResponse {

    Long id;
    String title;
    BigDecimal originalPrice;
    BigDecimal discountPrice;
    Integer discountRate;
    boolean hasDiscount = false;
    Gender gender;

}
