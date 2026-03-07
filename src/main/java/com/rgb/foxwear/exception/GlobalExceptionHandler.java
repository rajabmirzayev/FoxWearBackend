package com.rgb.foxwear.exception;

import com.rgb.foxwear.dto.ApiResponse;
import com.rgb.foxwear.enums.ErrorCode;
import lombok.NonNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<@NonNull ApiResponse<?>> handleMethodArgumentNotValidException(Errors errors) {
        Map<String, String> errorMap = new HashMap<>();

        errors.getFieldErrors().forEach(error -> errorMap.put(
                error.getField(),
                error.getDefaultMessage()
        ));

        var iterator = errorMap.entrySet().iterator();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.error(iterator.next().getValue(), ErrorCode.VALIDATION, errorMap));
    }

    @ExceptionHandler(InvalidArgumentException.class)
    public ResponseEntity<@NonNull ApiResponse<?>> handleInvalidArgumentException(InvalidArgumentException ex) {
        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_CONTENT)
                .body(ApiResponse.error(ex.getMessage(), ErrorCode.INVALID_ARGUMENT));
    }

    // Authentication error handlers
    @ExceptionHandler(PasswordMismatchException.class)
    public ResponseEntity<@NonNull ApiResponse<?>> handlePasswordMismatchException(PasswordMismatchException ex) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(ApiResponse.error(ex.getMessage(), ErrorCode.PASSWORD_MISMATCH));
    }

    @ExceptionHandler(UnderageUserException.class)
    public ResponseEntity<@NonNull ApiResponse<?>> handleUnderageUserException(UnderageUserException ex) {
        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_CONTENT)
                .body(ApiResponse.error(ex.getMessage(), ErrorCode.UNDERAGE));
    }

    // User error handlers
    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<@NonNull ApiResponse<?>> handleUserAlreadyExistsException(UserAlreadyExistsException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(ApiResponse.error(ex.getMessage(), ErrorCode.USER_ALREADY_EXISTS));
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<@NonNull ApiResponse<?>> handleUserNotFoundException(UserNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(ApiResponse.error(ex.getMessage(), ErrorCode.USER_NOT_FOUND));
    }
    
    // Product error handlers
    @ExceptionHandler(WearCategoryNotFound.class)
    public ResponseEntity<@NonNull ApiResponse<?>> handleWearCategoryNotFound(WearCategoryNotFound ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(ApiResponse.error(ex.getMessage(), ErrorCode.WEAR_CATEGORY_NOT_FOUND));
    }

    @ExceptionHandler(ProductSizeNotFoundException.class)
    public ResponseEntity<@NonNull ApiResponse<?>> handleProductSizeNotFoundException(ProductSizeNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(ApiResponse.error(ex.getMessage(), ErrorCode.PRODUCT_SIZE_NOT_FOUND));
    }

}
