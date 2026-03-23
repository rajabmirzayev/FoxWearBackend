package com.foxwear.productservice.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ColorOptionAllValuesResponse {

    Long id;
    String colorName;
    String colorCode;

}
