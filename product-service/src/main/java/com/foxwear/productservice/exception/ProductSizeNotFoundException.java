package com.foxwear.productservice.exception;

import com.foxwear.common.enums.ErrorCode;
import com.foxwear.common.exception.BaseException;
import org.springframework.http.HttpStatus;

public class ProductSizeNotFoundException extends BaseException {
    public ProductSizeNotFoundException(String message) {
        super(message, HttpStatus.NOT_FOUND, ErrorCode.PRODUCT_SIZE_NOT_FOUND);
    }
}
