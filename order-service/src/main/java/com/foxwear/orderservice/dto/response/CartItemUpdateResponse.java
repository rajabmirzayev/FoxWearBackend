package com.foxwear.orderservice.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CartItemUpdateResponse {

    Long id;
    Long productItemId;
    String productName;
    String colorName;
    String imageUrl;
    String sizeValue;
    Integer quantity;
    String slug;
    BigDecimal originalUnitPrice;
    BigDecimal actualUnitPrice;
    BigDecimal subTotal;

}
