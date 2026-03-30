package com.foxwear.authservice.exception;

import com.foxwear.common.enums.ErrorCode;
import com.foxwear.common.exception.BaseException;
import org.springframework.http.HttpStatus;

public class AddressNotFoundException extends BaseException {
    public AddressNotFoundException(String message) {
        super(message, HttpStatus.NOT_FOUND, ErrorCode.ADDRESS_NOT_FOUND);
    }
}
