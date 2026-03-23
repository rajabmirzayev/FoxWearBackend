package com.foxwear.productservice.exception;

import com.foxwear.common.enums.ErrorCode;
import com.foxwear.common.exception.BaseException;
import org.springframework.http.HttpStatus;

public class SizeAlreadyExistsException extends BaseException {
    public SizeAlreadyExistsException(String message) {
        super(message, HttpStatus.CONFLICT, ErrorCode.SIZE_ALREADY_EXISTS);
    }
}
