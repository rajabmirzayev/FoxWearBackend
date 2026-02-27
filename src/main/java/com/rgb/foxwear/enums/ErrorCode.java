package com.rgb.foxwear.enums;

import lombok.Getter;

@Getter
public enum ErrorCode {
    USER_ALREADY_EXISTS("USR_001"),

    VALIDATION("GEN_001"),

    PASSWORD_MISMATCH("AUTH_001"),
    UNDERAGE("AUTH_002"),;

    private final String code;

    ErrorCode(String code) {
        this.code = code;
    }
}
