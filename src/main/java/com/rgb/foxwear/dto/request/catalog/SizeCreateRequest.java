package com.rgb.foxwear.dto.request.catalog;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SizeCreateRequest {

    @NotBlank(message = "Size value is required")
    @Size(max = 10, message = "Size value must not exceed 10 characters")
    String sizeValue;

}
