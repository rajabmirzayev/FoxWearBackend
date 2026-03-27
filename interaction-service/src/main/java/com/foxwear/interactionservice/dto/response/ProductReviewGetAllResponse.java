package com.foxwear.interactionservice.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductReviewGetAllResponse {

    Long id;
    Long productId;
    Integer rate;
    String description;
    Long userId;
    Boolean isActive;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;

}
