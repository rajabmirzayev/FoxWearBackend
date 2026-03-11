package com.rgb.foxwear.dto.response.catalog;

import com.rgb.foxwear.entity.catalog.WearCategory;
import com.rgb.foxwear.enums.Gender;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductGetAllResponse {

    Long id;
    String title;
    BigDecimal originalPrice;
    BigDecimal discountPrice;
    Integer discountRate;
    boolean hasDiscount = false;
    String slug;
    Gender gender;
    CategoryGetAllResponse category;
    List<ColorOptionGetAllResponse> colors;

}
