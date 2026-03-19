package com.rgb.foxwear.exception;

public class ProductIsDeletedException extends RuntimeException {
    public ProductIsDeletedException(String message) {
        super(message);
    }
}
