package com.rgb.foxwear.dto.response.catalog;

import com.rgb.foxwear.enums.Gender;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductCreateResponse {

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
    List<ColorOptionCreateResponse> colors;

}
