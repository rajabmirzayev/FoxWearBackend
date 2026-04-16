package com.foxwear.orderservice.dto.response;

import com.foxwear.orderservice.enums.OrderStatus;
import com.foxwear.orderservice.enums.PaymentMethod;
import com.foxwear.orderservice.enums.PaymentStatus;
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
public class OrderGetResponse {

    Long id;
    String orderNumber;
    OrderStatus status;
    BigDecimal totalDiscountPrice;
    PaymentStatus paymentStatus;
    PaymentMethod paymentMethod;
    String addressSnapshot;
    Double latitudeSnapshot;
    Double longitudeSnapshot;
    String orderNote;
    String trackingNumber;
    LocalDateTime estimatedDeliveryDate;
    Long courierId;
    String phoneNumber;
    LocalDateTime pickedUpAt;
    LocalDateTime preparedAt;
    LocalDateTime deliveredAt;
    List<OrderItemGetResponse> items;

}
