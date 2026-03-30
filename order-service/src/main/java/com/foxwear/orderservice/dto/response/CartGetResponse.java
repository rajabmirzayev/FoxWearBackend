package com.foxwear.orderservice.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CartGetResponse {

    Long id;
    Long userId;
    List<CartItemGetResponse> items;
    BigDecimal totalPrice;

}
