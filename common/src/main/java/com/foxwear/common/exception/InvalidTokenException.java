package com.foxwear.common.exception;

import com.foxwear.common.enums.ErrorCode;
import org.springframework.http.HttpStatus;

public class InvalidTokenException extends BaseException {
    public InvalidTokenException(String message) {
        super(message, HttpStatus.UNAUTHORIZED, ErrorCode.INVALID_TOKEN);
    }
}
