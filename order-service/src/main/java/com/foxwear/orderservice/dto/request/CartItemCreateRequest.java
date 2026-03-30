package com.foxwear.orderservice.dto.request;

import com.foxwear.orderservice.entity.Cart;
import jakarta.persistence.*;
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
    @Column(name = "product_item_id", nullable = false)
    Long productItemId;

    @NotNull
    @Column(name = "product_name", nullable = false)
    String productName;

    @NotNull
    @Column(name = "color_name", nullable = false)
    String colorName;

    @NotNull
    @Column(name = "image_url", nullable = false)
    String imageUrl;

    @NotNull
    @Column(name = "size_value", nullable = false)
    String sizeValue;

    @NotNull
    @Min(0)
    @Column(nullable = false)
    Integer quantity;

    @NotNull
    @DecimalMin("0.0")
    @Column(name = "original_unit_price", nullable = false, precision = 12, scale = 2)
    BigDecimal originalUnitPrice;

    @NotNull
    @DecimalMin("0.0")
    @Column(name = "actual_unit_price", nullable = false, precision = 12, scale = 2)
    BigDecimal actualUnitPrice;

}
