package com.foxwear.authservice.exception;

import com.foxwear.common.enums.ErrorCode;
import com.foxwear.common.exception.BaseException;
import org.springframework.http.HttpStatus;

public class PasswordMismatchException extends BaseException {
    public PasswordMismatchException(String message) {
        super(message, HttpStatus.BAD_REQUEST, ErrorCode.PASSWORD_MISMATCH);
    }
}
