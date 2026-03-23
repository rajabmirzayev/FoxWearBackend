package com.foxwear.dynamicdataservice.exception;

import com.foxwear.common.enums.ErrorCode;
import com.foxwear.common.exception.BaseException;
import org.springframework.http.HttpStatus;

public class BannerAlreadyExistsException extends BaseException {
    public BannerAlreadyExistsException(String message) {
        super(message, HttpStatus.CONFLICT, ErrorCode.DYNAMIC_DATA_ALREADY_EXISTS);
    }
}
