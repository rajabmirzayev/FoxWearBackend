package com.foxwear.orderservice.controller;

import com.foxwear.common.dto.ApiResponse;
import com.foxwear.orderservice.dto.request.CartItemCreateRequest;
import com.foxwear.orderservice.dto.response.CartGetResponse;
import com.foxwear.orderservice.dto.response.CartItemCreateResponse;
import com.foxwear.orderservice.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/carts")
@RequiredArgsConstructor
public class CartController {
    private final CartService cartService;

    @PostMapping
    public ResponseEntity<ApiResponse<CartItemCreateResponse>> createCart(
            @RequestBody CartItemCreateRequest request,
            @RequestHeader(value = "X-User-Id") Long userId
    ) {
        var response = cartService.addItemToCart(request, userId);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<CartGetResponse>> getCart(
            @RequestHeader(value = "X-User-Id") Long userId
    ) {
        var response = cartService.getCart(userId);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

}
