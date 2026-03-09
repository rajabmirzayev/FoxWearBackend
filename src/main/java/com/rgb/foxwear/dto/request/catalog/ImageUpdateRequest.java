package com.rgb.foxwear.dto.request.catalog;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ImageUpdateRequest implements ImageDTO {

    @NotBlank(message = "Image URL is required")
    @Size(max = 10000, message = "Image URL must not exceed 10000 characters")
    String image;

    @NotNull(message = "isMain flag is required")
    Boolean isMain = false;

}
