package com.foxwear.orderservice.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SaleItemCreateResponse {

    Long id;
    Long productItemId;
    Integer quantity;
    BigDecimal unitPrice;
    BigDecimal totalPrice;

}
