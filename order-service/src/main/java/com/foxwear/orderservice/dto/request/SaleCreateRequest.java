package com.foxwear.orderservice.dto.request;

import com.foxwear.orderservice.enums.SalePaymentMethod;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SaleCreateRequest {

    @NotNull(message = "Total amount is required")
    BigDecimal totalAmount;

    SalePaymentMethod paymentMethod;

    @NotNull(message = "Items are required")
    List<SaleItemCreateRequest> items;

}
