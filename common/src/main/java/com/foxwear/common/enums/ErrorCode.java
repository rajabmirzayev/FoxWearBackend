package com.foxwear.common.enums;

import lombok.Getter;

/**
 * Enum representing application-wide error codes for consistent error handling.
 */
@Getter
public enum ErrorCode {

    // Wear and Product related errors
    WEAR_CATEGORY_NOT_FOUND("WEAR_001"),
    PRODUCT_SIZE_NOT_FOUND("WEAR_002"),
    PRODUCT_SIZE_ALREADY_EXISTS("WEAR_003"),
    PRODUCT_IS_DELETED("WEAR_004"),
    PRODUCT_NOT_FOUND("WEAR_005"),
    SIZE_ALREADY_EXISTS("WEAR_006"),
    WEAR_CATEGORY_ALREADY_EXISTS("WEAR_007"),

    // Validation and General errors
    VALIDATION("VALID_001"),
    INVALID_ARGUMENT("GEN_002"),

    // Authentication and Authorization errors
    UNAUTHORIZED("AUTH_001"),
    FORBIDDEN("AUTH_002"),
    USER_NOT_FOUND("AUTH_003"),
    USER_ALREADY_EXISTS("AUTH_004"),
    INVALID_TOKEN("AUTH_005"),
    TOKEN_EXPIRED("AUTH_006"),
    EMAIL_NOT_VERIFIED("AUTH_007"),
    PASSWORD_MISMATCH("AUTH_008"),
    UNDERAGE("AUTH_009"),

    // Dynamic Data related errors
    DYNAMIC_DATA_NOT_FOUND("DYNAMIC_001"),
    DYNAMIC_DATA_ALREADY_EXISTS("DYNAMIC_002"),
    DYNAMIC_DATA_IS_NOT_ACTIVE("DYNAMIC_003"),

    // Review related errors
    REVIEW_NOT_FOUND("RVW_001"),
    REVIEW_IS_NOT_ACTIVE("RVW_002"),

    // Server errors
    INTERNAL_SERVER_ERROR("SERVER_001");

    /**
     * Unique string identifier for the error.
     */
    private final String code;

    ErrorCode(String code) {
        this.code = code;
    }

}
