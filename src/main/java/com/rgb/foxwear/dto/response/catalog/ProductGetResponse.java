package com.rgb.foxwear.dto.response.catalog;

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
public class ProductGetResponse {

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
    boolean isDeleted;
    CategoryGetAllResponse category;
    List<ColorOptionGetResponse> colors;

}
