package com.foxwear.orderservice.exception;

import com.foxwear.common.enums.ErrorCode;
import com.foxwear.common.exception.BaseException;
import org.springframework.http.HttpStatus;

public class CouponMinAmountException extends BaseException {
    public CouponMinAmountException(String message) {
        super(message, HttpStatus.BAD_REQUEST, ErrorCode.COUPON_MIN_ORDER);
    }
}
