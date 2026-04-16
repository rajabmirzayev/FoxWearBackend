package com.foxwear.orderservice.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OrderItemCreateResponse {

    Long id;
    Long productItemId;
    String productName;
    String colorName;
    String imageUrl;
    String sizeValue;
    String slug;
    Integer quantity;
    BigDecimal priceAtPurchase;
    BigDecimal discountAtPurchase;
    BigDecimal subTotal;
    String skuSnapshot;
    boolean isReviewed;

}
