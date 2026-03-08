package com.rgb.foxwear.dto.response.catalog;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ColorOptionCreateResponse {

    Long id;
    String colorName;
    String colorCode;
    List<ImageCreateResponse> images;
    List<ItemCreateResponse> items;

}
