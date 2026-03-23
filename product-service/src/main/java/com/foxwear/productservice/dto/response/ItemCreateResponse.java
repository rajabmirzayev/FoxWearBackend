package com.foxwear.productservice.dto.response;

import com.foxwear.productservice.entity.ProductSize;
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
