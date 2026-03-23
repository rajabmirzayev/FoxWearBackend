package com.foxwear.common.exception;

import com.foxwear.common.dto.ApiResponse;
import com.foxwear.common.enums.ErrorCode;
import io.jsonwebtoken.ExpiredJwtException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BaseException.class)
    public ResponseEntity<ApiResponse<Void>> handleBaseException(BaseException ex) {
        return ResponseEntity.status(ex.getStatus())
                .body(ApiResponse.error(ex.getMessage(), ex.getErrorCode()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Map<String, String>>> handleValidation(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(e -> errors.put(e.getField(), e.getDefaultMessage()));

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.error("Validation error", ErrorCode.VALIDATION, errors));
    }

    @ExceptionHandler({
            BadCredentialsException.class,
            LockedException.class,
            DisabledException.class,
            ExpiredJwtException.class,
            AccessDeniedException.class
    })
    public ResponseEntity<ApiResponse<Void>> handleSecurityExceptions(Exception ex) {
        HttpStatus status = HttpStatus.UNAUTHORIZED;
        ErrorCode code = ErrorCode.UNAUTHORIZED;

        if (ex instanceof AccessDeniedException) {
            status = HttpStatus.FORBIDDEN;
            code = ErrorCode.FORBIDDEN;
        } else if (ex instanceof DisabledException) {
            status = HttpStatus.FORBIDDEN;
            code = ErrorCode.EMAIL_NOT_VERIFIED;
        }

        return ResponseEntity.status(status)
                .body(ApiResponse.error(ex.getMessage(), code));
    }

//    @ExceptionHandler(Exception.class)
//    public ResponseEntity<ApiResponse<Void>> handleGeneralException() {
//        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
//                .body(ApiResponse.error("An unexpected error occurred", ErrorCode.INTERNAL_SERVER_ERROR));
//    }
}