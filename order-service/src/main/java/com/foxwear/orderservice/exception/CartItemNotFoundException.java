package com.foxwear.orderservice.exception;

public class CartItemNotFoundException extends RuntimeException {
  public CartItemNotFoundException(String message) {
    super(message);
  }
}
