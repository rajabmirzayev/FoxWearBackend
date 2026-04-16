package com.foxwear.orderservice.dto.response;

import com.foxwear.orderservice.entity.Order;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OrderItemGetResponse {

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
    boolean isReviewed;

}
