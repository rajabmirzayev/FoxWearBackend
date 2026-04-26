package com.foxwear.orderservice.dto.response;

import com.foxwear.orderservice.enums.OrderStatus;
import com.foxwear.orderservice.enums.PaymentMethod;
import com.foxwear.orderservice.enums.PaymentStatus;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OrderGetAllResponse {

    Long id;
    String orderNumber;
    Long userId;
    OrderStatus status;
    BigDecimal totalDiscountPrice;
    PaymentStatus paymentStatus;
    PaymentMethod paymentMethod;
    String addressSnapshot;
    String phoneNumber;
    Double latitudeSnapshot;
    Double longitudeSnapshot;
    LocalDateTime deliveredAt;

}
