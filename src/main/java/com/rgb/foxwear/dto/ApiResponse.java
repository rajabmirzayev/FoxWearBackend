package com.rgb.foxwear.dto;

import com.rgb.foxwear.enums.ErrorCode;
import lombok.Builder;
import lombok.Getter;

import static com.rgb.foxwear.util.Constant.SUCCESS_MESSAGE;

@Getter
@Builder
public class ApiResponse<T> {
    boolean success;
    String message;
    String errorCode;
    T data;

    public static <T> ApiResponse<T> success(T data) {
        return ApiResponse.<T>builder()
                .success(true)
                .message(SUCCESS_MESSAGE)
                .errorCode(null)
                .data(data)
                .build();
    }

    public static <T> ApiResponse<T> success(T data, String message) {
        return ApiResponse.<T>builder()
                .success(true)
                .message(message)
                .errorCode(null)
                .data(data)
                .build();
    }

    public static <T> ApiResponse<T> error(String message, ErrorCode errorCode) {
        return ApiResponse.<T>builder()
                .success(false)
                .message(message)
                .errorCode(errorCode.getCode())
                .build();
    }

    public static <T> ApiResponse<T> error(String message, ErrorCode errorCode, T data) {
        return ApiResponse.<T>builder()
                .success(false)
                .message(message)
                .errorCode(errorCode.getCode())
                .data(data)
                .build();
    }
}
