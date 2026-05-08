package com.foxwear.orderservice.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TopProductResponse {

    Long productItemId;
    String productName;
    String imageUrl;
    String productSlug;
    Integer totalQuantity;
    BigDecimal totalRevenue;

}
