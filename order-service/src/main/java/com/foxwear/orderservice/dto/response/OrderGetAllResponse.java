package com.foxwear.orderservice.dto.response;

import com.foxwear.orderservice.enums.OrderStatus;
import com.foxwear.orderservice.enums.PaymentMethod;
import com.foxwear.orderservice.enums.PaymentStatus;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;

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
    BigDecimal shippingFee;
    PaymentStatus paymentStatus;
    PaymentMethod paymentMethod;
    String addressSnapshot;
    String phoneNumber;
    Double latitudeSnapshot;
    Double longitudeSnapshot;

}
