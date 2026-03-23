package com.foxwear.dynamicdataservice.exception;

import com.foxwear.common.enums.ErrorCode;
import com.foxwear.common.exception.BaseException;
import org.springframework.http.HttpStatus;

public class BannerIsNotActiveException extends BaseException {
    public BannerIsNotActiveException(String message) {
        super(message, HttpStatus.UNPROCESSABLE_ENTITY, ErrorCode.DYNAMIC_DATA_IS_NOT_ACTIVE);
    }
}
