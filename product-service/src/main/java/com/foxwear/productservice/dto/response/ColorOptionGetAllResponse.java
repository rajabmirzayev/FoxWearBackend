package com.foxwear.productservice.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ColorOptionGetAllResponse {

    Long id;
    String colorName;
    String colorCode;
    List<ImageGetAllResponse> images;
    List<ItemGetAllResponse> items;

}