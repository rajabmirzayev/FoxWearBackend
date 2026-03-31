package com.foxwear.common.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductResponse {

    Long id;
    String title;
    BigDecimal originalPrice;
    BigDecimal discountPrice;
    Integer discountRate;
    String slug;
    String imageUrl;
    String color;
    String size;

}
