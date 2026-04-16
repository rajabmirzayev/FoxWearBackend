package com.foxwear.orderservice.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CartGetResponse {

    Long id;
    Long userId;
    List<CartItemGetResponse> items;
    CouponGetResponse coupon;
    Boolean couponApplied;
    BigDecimal totalOriginalPrice;
    BigDecimal totalPrice;
    BigDecimal shippingFee;

}
