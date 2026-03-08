package com.rgb.foxwear.enums;

import lombok.Getter;

@Getter
public enum ErrorCode {
    WEAR_CATEGORY_NOT_FOUND("WEAR_001"),
    PRODUCT_SIZE_NOT_FOUND("WEAR_002"),
    PRODUCT_NOT_FOUND("WEAR_004"),

    VALIDATION("VALID_001"),
    INVALID_ARGUMENT("GEN_002"),

    UNAUTHORIZED("AUTH_001"),
    FORBIDDEN("AUTH_002"),
    USER_NOT_FOUND("AUTH_003"),
    USER_ALREADY_EXISTS("AUTH_004"),
    INVALID_TOKEN("AUTH_005"),
    TOKEN_EXPIRED("AUTH_006"),
    EMAIL_NOT_VERIFIED("AUTH_007"),
    PASSWORD_MISMATCH("AUTH_008"),
    UNDERAGE("AUTH_009");

    private final String code;

    ErrorCode(String code) {
        this.code = code;
    }
}
