package com.foxwear.productservice.exception;

import com.foxwear.common.enums.ErrorCode;
import com.foxwear.common.exception.BaseException;
import org.springframework.http.HttpStatus;

public class WearCategoryAlreadyExistsException extends BaseException {
    public WearCategoryAlreadyExistsException(String message) {
        super(message, HttpStatus.CONFLICT, ErrorCode.WEAR_CATEGORY_ALREADY_EXISTS);
    }
}
