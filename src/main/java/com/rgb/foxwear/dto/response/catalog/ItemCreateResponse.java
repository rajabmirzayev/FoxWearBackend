package com.rgb.foxwear.dto.response.catalog;

import com.rgb.foxwear.entity.catalog.ProductSize;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ItemCreateResponse {

    Long id;
    ProductSize productSize;
    String sku;
    Integer stockQuantity;
    Integer stockRemaining;

}
