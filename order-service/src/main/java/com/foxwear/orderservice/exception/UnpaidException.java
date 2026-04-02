package com.foxwear.orderservice.exception;

import com.foxwear.common.enums.ErrorCode;
import com.foxwear.common.exception.BaseException;
import org.springframework.http.HttpStatus;

public class UnpaidException extends BaseException {
    public UnpaidException(String message) {
        super(message, HttpStatus.UNPROCESSABLE_ENTITY, ErrorCode.UNPAID);
    }
}
