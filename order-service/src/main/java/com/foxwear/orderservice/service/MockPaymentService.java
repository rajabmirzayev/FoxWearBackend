package com.foxwear.orderservice.service;

import com.foxwear.orderservice.dto.request.OrderCreateRequest;
import com.foxwear.orderservice.exception.PaymentException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@Slf4j
public class MockPaymentService implements PaymentService {
    @Override
    public boolean process(BigDecimal amount, OrderCreateRequest paymentDetails) {
        log.info("Payment processing");

        if (paymentDetails.getCardNumber().startsWith("5522")) {
            log.info("Payment successful");
            return true;
        }

        log.error("Invalid card number");
        throw new PaymentException("Error during payment");
    }
}
