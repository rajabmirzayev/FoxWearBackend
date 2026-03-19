package com.rgb.foxwear.dto.response.catalog;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ItemGetResponse {

    Long id;
    SizeResponse productSize;
    String sku;
    Integer stockQuantity;
    Integer stockRemaining;
    boolean isDeleted;

}
