package com.rgb.foxwear.enums;

import lombok.Getter;

@Getter
public enum ErrorCode {
    USER_ALREADY_EXISTS("USR_001"),
    USER_NOT_FOUND("USR_002"),

    WEAR_CATEGORY_NOT_FOUND("WEAR_001"),

    VALIDATION("GEN_001"),
    INVALID_ARGUMENT("GEN_002"),

    PASSWORD_MISMATCH("AUTH_001"),
    UNDERAGE("AUTH_002"),;

    private final String code;

    ErrorCode(String code) {
        this.code = code;
    }
}
