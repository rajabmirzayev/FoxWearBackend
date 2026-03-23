package com.foxwear.productservice.exception;

import com.foxwear.common.enums.ErrorCode;
import com.foxwear.common.exception.BaseException;
import org.springframework.http.HttpStatus;

public class ProductSizeAlreadyExistException extends BaseException {
    public ProductSizeAlreadyExistException(String message) {
        super(message, HttpStatus.CONFLICT, ErrorCode.PRODUCT_SIZE_ALREADY_EXISTS);
    }
}
