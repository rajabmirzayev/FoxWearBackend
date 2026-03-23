package com.foxwear.common.exception;

import com.foxwear.common.enums.ErrorCode;
import org.springframework.http.HttpStatus;

public class InvalidArgumentException extends BaseException {
    public InvalidArgumentException(String message) {
        super(message, HttpStatus.UNPROCESSABLE_ENTITY, ErrorCode.INVALID_ARGUMENT);
    }
}
