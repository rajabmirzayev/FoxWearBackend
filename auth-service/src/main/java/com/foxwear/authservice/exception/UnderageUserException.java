package com.foxwear.authservice.exception;

import com.foxwear.common.enums.ErrorCode;
import com.foxwear.common.exception.BaseException;
import org.springframework.http.HttpStatus;

public class UnderageUserException extends BaseException {
    public UnderageUserException(String message) {
        super(message, HttpStatus.UNPROCESSABLE_ENTITY, ErrorCode.UNDERAGE);
    }
}
