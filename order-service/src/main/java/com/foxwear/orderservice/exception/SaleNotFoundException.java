package com.foxwear.orderservice.exception;

import com.foxwear.common.enums.ErrorCode;
import com.foxwear.common.exception.BaseException;
import org.springframework.http.HttpStatus;

public class SaleNotFoundException extends BaseException {
    public SaleNotFoundException(String message) {
        super(message, HttpStatus.NOT_FOUND, ErrorCode.SALE_NOT_FOUND);
    }
}
