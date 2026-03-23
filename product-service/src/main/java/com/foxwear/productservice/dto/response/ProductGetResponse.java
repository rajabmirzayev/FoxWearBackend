package com.foxwear.productservice.dto.response;

import com.foxwear.common.enums.Gender;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.time.LocalDateTime;
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
    boolean isLiked;
    CategoryResponse category;
    List<ColorOptionGetResponse> colors;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;

}
