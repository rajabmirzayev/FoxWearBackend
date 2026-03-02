package com.rgb.foxwear.dto.response.catalog;

import com.rgb.foxwear.entity.catalog.WearCategory;
import com.rgb.foxwear.enums.Gender;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CreateProductResponse {

    Long id;
    String title;
    BigDecimal originalPrice;
    BigDecimal discountPrice;
    Integer discountRate;
    boolean hasDiscount;
    String slug;
    Gender gender;
    String description;
    boolean isActive;
    CreateCategoryResponse category;

}
