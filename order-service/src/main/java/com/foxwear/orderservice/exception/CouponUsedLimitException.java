package com.foxwear.orderservice.exception;

import com.foxwear.common.enums.ErrorCode;
import com.foxwear.common.exception.BaseException;
import org.springframework.http.HttpStatus;

public class CouponUsedLimitException extends BaseException {
    public CouponUsedLimitException(String message) {
        super(message, HttpStatus.BAD_REQUEST, ErrorCode.COUPON_USED_LIMIT);
    }
}
