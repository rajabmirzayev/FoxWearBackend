package com.foxwear.interactionservice.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductReviewCreateRequest {

    @NotNull
    Long productId;

    @NotNull
    @Min(1)
    @Max(5)
    Integer rate;

    @NotBlank
    String description;

}
