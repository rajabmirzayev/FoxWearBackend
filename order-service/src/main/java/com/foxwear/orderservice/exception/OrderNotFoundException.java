package com.foxwear.orderservice.exception;

import com.foxwear.common.enums.ErrorCode;
import com.foxwear.common.exception.BaseException;
import org.springframework.http.HttpStatus;

public class OrderNotFoundException extends BaseException {
    public OrderNotFoundException(String message) {
        super(message, HttpStatus.NOT_FOUND, ErrorCode.ORDER_NOT_FOUND);
    }
}
