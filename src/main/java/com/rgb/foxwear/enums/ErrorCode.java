package com.rgb.foxwear.enums;

import lombok.Getter;

@Getter
public enum ErrorCode {
    WEAR_CATEGORY_NOT_FOUND("WEAR_001"),
    PRODUCT_SIZE_NOT_FOUND("WEAR_002"),

    VALIDATION("GEN_001"),
    INVALID_ARGUMENT("GEN_002"),

    PASSWORD_MISMATCH("AUTH_001"),
    UNDERAGE("AUTH_002"),
    USER_NOT_FOUND("AUTH_003"),
    USER_ALREADY_EXISTS("AUTH_004");

    private final String code;

    ErrorCode(String code) {
        this.code = code;
    }
}
