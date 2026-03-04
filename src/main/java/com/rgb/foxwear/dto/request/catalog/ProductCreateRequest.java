package com.rgb.foxwear.dto.request.catalog;

import com.rgb.foxwear.enums.Gender;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class ProductCreateRequest {

    @NotBlank(message = "Product title is required")
    @Size(max = 100, message = "Product title must not exceed 100 characters")
    String title;

    @NotNull(message = "Original price is required")
    @DecimalMin(value = "0.0", inclusive = false, message = "Original price must be greater than 0")
    BigDecimal originalPrice;

    @DecimalMin(value = "0.0", inclusive = false, message = "Discounted price must be greater than 0")
    BigDecimal discountPrice;

    @Min(value = 0, message = "Discount rate cannot be less than 0")
    @Max(value = 100, message = "Discount rate cannot exceed 100")
    Integer discountRate;

    @NotNull(message = "Gender is required")
    Gender gender;

    String description;

    @NotNull(message = "Category is required")
    Long categoryId;
    
    List<ColorOptionCreateRequest> colors;

}
