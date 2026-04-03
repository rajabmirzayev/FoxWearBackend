package com.foxwear.orderservice.exception;

import com.foxwear.common.enums.ErrorCode;
import com.foxwear.common.exception.BaseException;
import org.springframework.http.HttpStatus;

public class CouponAlreadyExistsException extends BaseException {
    public CouponAlreadyExistsException(String message) {
        super(message, HttpStatus.CONFLICT, ErrorCode.COUPON_ALREADY_EXISTS);
    }
}
