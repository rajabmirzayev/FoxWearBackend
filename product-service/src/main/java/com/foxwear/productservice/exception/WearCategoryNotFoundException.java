package com.foxwear.productservice.exception;

import com.foxwear.common.enums.ErrorCode;
import com.foxwear.common.exception.BaseException;
import org.springframework.http.HttpStatus;

public class WearCategoryNotFoundException extends BaseException {
    public WearCategoryNotFoundException(String message) {
        super(message, HttpStatus.NOT_FOUND, ErrorCode.WEAR_CATEGORY_NOT_FOUND);
    }
}
