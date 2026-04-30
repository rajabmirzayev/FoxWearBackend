package com.foxwear.orderservice.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SaleItemCreateRequest {

    @NotNull(message = "Product item ID is required")
    Long productItemId;

    @NotNull(message = "Quantity is required")
    Integer quantity;

    @NotNull(message = "Unit price is required")
    BigDecimal unitPrice;

    @NotNull(message = "Total price is required")
    BigDecimal totalPrice;

}
