package com.rgb.foxwear.dto.response.catalog;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ImageCreateResponse {

    Long id;
    String image;
    Boolean isMain;

}
