package com.foxwear.interactionservice.exception;

import com.foxwear.common.enums.ErrorCode;
import com.foxwear.common.exception.BaseException;
import org.springframework.http.HttpStatus;

public class ReviewIsNotActiveException extends BaseException {
    public ReviewIsNotActiveException(String message) {
        super(message, HttpStatus.UNPROCESSABLE_ENTITY, ErrorCode.REVIEW_IS_NOT_ACTIVE);
    }
}
