package com.rgb.foxwear.dto.request.catalog;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CategoryRequest {

    @NotBlank(message = "Category name is required")
    @Size(max = 30, message = "Category name must not exceed 30 characters")
    String name;

    @NotBlank(message = "Category subtitle is required")
    @Size(max = 200, message = "Category subtitle must not exceed 200 characters")
    String subtitle;

    @Size(max = 255, message = "Category link must not exceed 255 characters")
    String link;

    @Size(max = 10000, message = "Category image URL is too long")
    String mainImage;

    Long parentId;

}
