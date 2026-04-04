package com.foxwear.orderservice.dto.response;

import com.foxwear.orderservice.entity.Coupon;
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
    Coupon coupon;
    Boolean couponApplied;
    BigDecimal totalOriginalPrice;
    BigDecimal totalPrice;
    BigDecimal shippingFee;

}
