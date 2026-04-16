package com.foxwear.orderservice.exception;

import com.foxwear.common.enums.ErrorCode;
import com.foxwear.common.exception.BaseException;
import org.springframework.http.HttpStatus;

public class PaymentException extends BaseException {
    public PaymentException(String message) {
        super(message, HttpStatus.PAYMENT_REQUIRED, ErrorCode.PAYMENT_ERROR);
    }
}
