package com.rgb.foxwear.enums;

import lombok.Getter;

@Getter
public enum ErrorCode {
    USER_ALREADY_EXISTS("USR_001");

    private final String code;

    ErrorCode(String code) {
        this.code = code;
    }
}
