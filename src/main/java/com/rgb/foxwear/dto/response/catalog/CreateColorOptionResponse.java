package com.rgb.foxwear.dto.response.catalog;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CreateColorOptionResponse {

    Long id;
    String colorName;
    String colorCode;
    GetAllProductResponse product;

}
