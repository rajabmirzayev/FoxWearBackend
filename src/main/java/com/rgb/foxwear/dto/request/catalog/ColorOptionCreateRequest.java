package com.rgb.foxwear.dto.request.catalog;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ColorOptionCreateRequest implements ColorOptionDTO {

    @NotBlank(message = "Color name is required")
    @Size(max = 30, message = "Color name must not exceed 30 characters")
    String colorName;

    @NotBlank(message = "Color code is required")
    @Size(max = 30, message = "Color code must not exceed 30 characters")
    String colorCode;

    List<@Valid ImageCreateRequest> images;

    List<@Valid ItemCreateRequest> items;

}
