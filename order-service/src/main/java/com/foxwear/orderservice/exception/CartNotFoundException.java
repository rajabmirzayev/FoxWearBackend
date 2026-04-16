package com.foxwear.orderservice.exception;

import com.foxwear.common.enums.ErrorCode;
import com.foxwear.common.exception.BaseException;
import org.springframework.http.HttpStatus;

public class CartNotFoundException extends BaseException {
    public CartNotFoundException(String message) {
        super(message, HttpStatus.NOT_FOUND, ErrorCode.CART_NOT_FOUND);
    }
}
