package com.foxwear.orderservice.exception;

import com.foxwear.common.enums.ErrorCode;
import com.foxwear.common.exception.BaseException;
import org.springframework.http.HttpStatus;

public class CouponNotFoundException extends BaseException {
    public CouponNotFoundException(String message) {
        super(message, HttpStatus.NOT_FOUND, ErrorCode.COUPON_NOT_FOUND);
    }
}
