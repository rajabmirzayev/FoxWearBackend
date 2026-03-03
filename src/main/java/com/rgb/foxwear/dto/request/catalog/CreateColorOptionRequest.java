package com.rgb.foxwear.dto.request.catalog;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CreateColorOptionRequest {

    @NotBlank(message = "Color name is required")
    @Size(max = 30, message = "Color name must be at most 30 characters")
    String colorName;

    @NotBlank(message = "Color code is required")
    @Size(max = 30, message = "Color code must be at most 30 characters")
    String colorCode;

    @NotNull(message = "Product ID is required")
    Long productId;

}
