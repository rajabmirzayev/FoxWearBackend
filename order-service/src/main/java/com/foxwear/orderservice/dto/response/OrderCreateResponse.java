package com.foxwear.orderservice.dto.response;

import com.foxwear.orderservice.entity.OrderItem;
import com.foxwear.orderservice.enums.OrderStatus;
import com.foxwear.orderservice.enums.PaymentMethod;
import com.foxwear.orderservice.enums.PaymentStatus;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OrderCreateResponse {

    Long id;
    String orderNumber;
    Long userId;
    OrderStatus status;
    BigDecimal totalDiscountPrice;
    Long couponId;
    BigDecimal shippingFee;
    PaymentStatus paymentStatus;
    PaymentMethod paymentMethod;
    String addressSnapshot;
    Double latitudeSnapshot;
    Double longitudeSnapshot;
    String orderNote;
    String trackingNumber;
    LocalDateTime estimatedDeliveryDate;
    Long courierId;
    LocalDateTime pickedUpAt;
    LocalDateTime preparedAt;
    LocalDateTime deliveredAt;
    List<OrderItemCreateResponse> items;

}
