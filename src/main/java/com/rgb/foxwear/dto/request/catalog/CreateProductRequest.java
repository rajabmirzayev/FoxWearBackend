package com.rgb.foxwear.dto.request.catalog;

import com.rgb.foxwear.enums.Gender;
import jakarta.validation.constraints.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CreateProductRequest {

    @NotBlank(message = "Product title is required")
    @Size(max = 100, message = "Product title cannot exceed 100 characters")
    @Pattern(regexp = "^[a-zA-Z\\s]*$", message = "Product title must contain only letters")
    String title;

    @NotNull(message = "Original price is required")
    @DecimalMin(value = "0.0", inclusive = false, message = "Original price must be greater than zero")
    BigDecimal originalPrice;

    BigDecimal discountPrice;

    @Min(value = 0, message = "Discount rate cannot be less than 0")
    @Max(value = 100, message = "Discount rate cannot exceed 100")
    Integer discountRate;

    @NotBlank(message = "Product slug is required")
    @Size(max = 100, message = "Slug cannot exceed 100 characters")
    String slug;

    @NotNull(message = "Gender category is required")
    Gender gender;

    String description;

    @NotNull(message = "Category ID is required")
    Long categoryId;

}
