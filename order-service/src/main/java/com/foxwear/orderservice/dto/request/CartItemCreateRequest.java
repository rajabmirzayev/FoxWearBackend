package com.foxwear.orderservice.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CartItemCreateRequest {

    @NotNull
    Long productItemId;

    @NotNull
    @Min(0)
    Integer quantity;

}
