package com.foxwear.productservice.exception;

import com.foxwear.common.enums.ErrorCode;
import com.foxwear.common.exception.BaseException;
import org.springframework.http.HttpStatus;

public class ProductIsDeletedException extends BaseException {
    public ProductIsDeletedException(String message) {
        super(message, HttpStatus.GONE, ErrorCode.PRODUCT_IS_DELETED);
    }
}
