package com.foxwear.orderservice.exception;

import com.foxwear.common.enums.ErrorCode;
import com.foxwear.common.exception.BaseException;
import org.springframework.http.HttpStatus;

public class CouponIsNotActiveException extends BaseException {
    public CouponIsNotActiveException(String message) {
        super(message, HttpStatus.UNPROCESSABLE_ENTITY, ErrorCode.COUPON_IS_NOT_ACTIVE);
    }
}
