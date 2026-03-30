package com.foxwear.interactionservice.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductReviewCreateResponse {

    Long id;
    Long productId;
    Integer rate;
    String description;
    Long userId;
    Boolean isActive;

}
