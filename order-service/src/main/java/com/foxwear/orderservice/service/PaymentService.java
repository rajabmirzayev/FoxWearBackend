package com.foxwear.orderservice.service;

import com.foxwear.orderservice.dto.request.OrderCreateRequest;

import java.math.BigDecimal;

public interface PaymentService {
    boolean process(BigDecimal amount, OrderCreateRequest paymentDetails);
}
