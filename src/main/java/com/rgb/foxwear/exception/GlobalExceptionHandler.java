package com.rgb.foxwear.exception;

import com.rgb.foxwear.dto.ApiResponse;
import com.rgb.foxwear.enums.ErrorCode;
import lombok.NonNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
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

    @ExceptionHandler(InvalidTokenException.class)
    public ResponseEntity<@NonNull ApiResponse<Void>> handleInvalidToken(InvalidTokenException ex) {
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(ApiResponse.error(ex.getMessage(), ErrorCode.INVALID_TOKEN));
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<@NonNull ApiResponse<Void>> handleBadCredentials(BadCredentialsException ex) {
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(ApiResponse.error(ex.getMessage(), ErrorCode.UNAUTHORIZED));
    }

    @ExceptionHandler(DisabledException.class)
    public ResponseEntity<@NonNull ApiResponse<Void>> handleDisabled(DisabledException ex) {
        return ResponseEntity
                .status(HttpStatus.FORBIDDEN)
                .body(ApiResponse.error(ex.getMessage(), ErrorCode.EMAIL_NOT_VERIFIED));
    }

    @ExceptionHandler(LockedException.class)
    public ResponseEntity<@NonNull ApiResponse<Void>> handleLocked(LockedException ex) {
        return ResponseEntity
                .status(HttpStatus.FORBIDDEN)
                .body(ApiResponse.error(ex.getMessage(), ErrorCode.FORBIDDEN));
    }

    // Product error handlers
    @ExceptionHandler(WearCategoryNotFoundException.class)
    public ResponseEntity<@NonNull ApiResponse<?>> handleWearCategoryNotFound(WearCategoryNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(ApiResponse.error(ex.getMessage(), ErrorCode.WEAR_CATEGORY_NOT_FOUND));
    }

    @ExceptionHandler(ProductSizeNotFoundException.class)
    public ResponseEntity<@NonNull ApiResponse<?>> handleProductSizeNotFoundException(ProductSizeNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(ApiResponse.error(ex.getMessage(), ErrorCode.PRODUCT_SIZE_NOT_FOUND));
    }

    @ExceptionHandler(ProductIsDeletedException.class)
    public ResponseEntity<@NonNull ApiResponse<?>> handleProductIsDeletedException(ProductIsDeletedException ex) {
        return ResponseEntity.status(HttpStatus.GONE)
                .body(ApiResponse.error(ex.getMessage(), ErrorCode.PRODUCT_IS_DELETED));
    }

    @ExceptionHandler(ProductNotFoundException.class)
    public ResponseEntity<@NonNull ApiResponse<?>> handleProductNotFoundException(ProductNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(ApiResponse.error(ex.getMessage(), ErrorCode.PRODUCT_NOT_FOUND));
    }

    @ExceptionHandler(SizeAlreadyExistsException.class)
    public ResponseEntity<@NonNull ApiResponse<?>> handleSizeAlreadyExistsException(SizeAlreadyExistsException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(ApiResponse.error(ex.getMessage(), ErrorCode.SIZE_ALREADY_EXISTS));
    }

    @ExceptionHandler(WearCategoryAlreadyExistsException.class)
    public ResponseEntity<@NonNull ApiResponse<?>> handleWearCategoryAlreadyExistsException(WearCategoryAlreadyExistsException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(ApiResponse.error(ex.getMessage(), ErrorCode.WEAR_CATEGORY_ALREADY_EXISTS));
    }
}
