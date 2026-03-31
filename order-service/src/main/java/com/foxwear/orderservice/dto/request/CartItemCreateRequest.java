package com.foxwear.orderservice.dto.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CartItemCreateRequest {

    @NotNull
    Long productItemId;

    @NotNull
    String productName;

    @NotNull
    String colorName;

    @NotNull
    String imageUrl;

    @NotNull
    String sizeValue;

    @NotNull
    @Min(0)
    Integer quantity;

    @NotNull
    @DecimalMin("0.0")
    BigDecimal originalUnitPrice;

    @NotNull
    @DecimalMin("0.0")
    BigDecimal actualUnitPrice;

}
