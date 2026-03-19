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
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ImageCreateRequest implements ImageDTO {

    @NotBlank(message = "Image is required")
    @Size(max = 10000, message = "Image URL must not exceed 10000 characters")
    String image;

    @NotNull(message = "Main flag is required")
    Boolean isMain = false;

}
