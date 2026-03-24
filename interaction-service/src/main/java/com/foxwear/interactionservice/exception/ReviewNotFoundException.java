package com.foxwear.interactionservice.exception;

import com.foxwear.common.enums.ErrorCode;
import com.foxwear.common.exception.BaseException;
import org.springframework.http.HttpStatus;

public class ReviewNotFoundException extends BaseException {
    public ReviewNotFoundException(String message) {
        super(message, HttpStatus.NOT_FOUND, ErrorCode.REVIEW_NOT_FOUND);
    }
}
