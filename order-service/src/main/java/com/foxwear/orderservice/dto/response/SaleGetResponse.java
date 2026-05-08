package com.foxwear.orderservice.dto.response;

import com.foxwear.orderservice.enums.SalePaymentMethod;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SaleGetResponse {

    Long id;
    String receiptNumber;
    Long cashierId;
    BigDecimal totalAmount;
    SalePaymentMethod paymentMethod;
    List<SaleItemGetResponse> items;
    LocalDateTime createdAt;

}
