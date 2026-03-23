package com.foxwear.productservice.dto.response;

import com.foxwear.common.enums.Gender;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductUpdateResponse {

    Long id;
    String title;
    BigDecimal originalPrice;
    BigDecimal discountPrice;
    Integer discountRate;
    Boolean hasDiscount;
    String slug;
    Gender gender;
    String description;
    String categoryName;
    List<ColorOptionUpdateResponse> colors;

}
