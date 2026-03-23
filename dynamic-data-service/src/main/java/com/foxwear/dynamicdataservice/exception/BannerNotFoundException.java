package com.foxwear.dynamicdataservice.exception;

import com.foxwear.common.enums.ErrorCode;
import com.foxwear.common.exception.BaseException;
import org.springframework.http.HttpStatus;

public class BannerNotFoundException extends BaseException {
    public BannerNotFoundException(String message) {
        super(message, HttpStatus.NOT_FOUND, ErrorCode.DYNAMIC_DATA_NOT_FOUND);
    }
}
