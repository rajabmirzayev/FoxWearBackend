package com.foxwear.interactionservice.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SiteReviewGetAllResponse {

    Long id;
    Integer rate;
    String description;
    Long userId;
    boolean isActive = true;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;


}
