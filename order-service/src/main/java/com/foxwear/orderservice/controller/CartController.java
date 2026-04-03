package com.foxwear.orderservice.controller;

import com.foxwear.common.dto.ApiResponse;
import com.foxwear.orderservice.dto.request.CartItemCreateRequest;
import com.foxwear.orderservice.dto.response.CartGetResponse;
import com.foxwear.orderservice.dto.response.CartItemCreateResponse;
import com.foxwear.orderservice.dto.response.CartItemUpdateResponse;
import com.foxwear.orderservice.service.CartService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/carts")
@RequiredArgsConstructor
@Tag(name = "Cart Management", description = "Endpoints for managing user shopping carts")
public class CartController {
    private final CartService cartService;

    @Operation(summary = "Add item to cart", description = "Adds a new item or increases quantity if item already exists in the cart")
    @PostMapping
    public ResponseEntity<ApiResponse<CartItemCreateResponse>> createCart(
            @Valid @RequestBody CartItemCreateRequest request,
            @RequestHeader(value = "X-User-Id") Long userId
    ) {
        var response = cartService.addItemToCart(request, userId);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @Operation(summary = "Get user cart", description = "Retrieves all items currently in the user's cart")
    @GetMapping
    public ResponseEntity<ApiResponse<CartGetResponse>> getCart(
            @RequestHeader(value = "X-User-Id") Long userId
    ) {
        var response = cartService.getCart(userId);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @Operation(summary = "Get cart items count", description = "Returns the total quantity of all items in the user's cart")
    @GetMapping("/count")
    public ResponseEntity<ApiResponse<Integer>> getCartCount(
            @RequestHeader(value = "X-User-Id") Long userId
    ) {
        int count = cartService.getCartCount(userId);
        return ResponseEntity.ok(ApiResponse.success(count));
    }

    @Operation(summary = "Increase item quantity", description = "Increments the quantity of a specific item in the cart by 1")
    @PatchMapping("/increase/{itemId}")
    public ResponseEntity<ApiResponse<CartItemUpdateResponse>> increaseItemQuantity(
            @Parameter(description = "The ID of the cart item to increase") @PathVariable Long itemId,
            @RequestHeader(value = "X-User-Id") Long userId
    ) {
        var response = cartService.increaseQuantity(itemId, userId);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @Operation(summary = "Decrease item quantity", description = "Decrements the quantity of a specific item in the cart by 1")
    @PatchMapping("/decrease/{itemId}")
    public ResponseEntity<ApiResponse<CartItemUpdateResponse>> decreaseItemQuantity(
            @Parameter(description = "The ID of the cart item to decrease") @PathVariable Long itemId,
            @RequestHeader(value = "X-User-Id") Long userId
    ) {
        var response = cartService.decreaseQuantity(itemId, userId);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @PatchMapping("/coupon/{code}")
    public ResponseEntity<ApiResponse<CartItemUpdateResponse>> applyCoupon(
            @PathVariable String code,
            @RequestHeader(value = "X-User-Id") Long userId
    ) {
        cartService.applyCoupon(code, userId);
        return ResponseEntity.ok(ApiResponse.success(null));
    }

    @Operation(summary = "Remove item from cart", description = "Deletes a specific item from the user's shopping cart")
    @DeleteMapping("{itemId}")
    public ResponseEntity<ApiResponse<Void>> deleteItem(
            @Parameter(description = "The ID of the cart item to delete") @PathVariable Long itemId,
            @RequestHeader(value = "X-User-Id") Long userId
    ) {
        cartService.deleteItem(itemId, userId);
        return ResponseEntity.ok(ApiResponse.success(null));
    }

    @Operation(summary = "Clear all items", description = "Removes all items from the user's cart")
    @DeleteMapping
    public ResponseEntity<ApiResponse<Void>> clearCart(
            @RequestHeader(value = "X-User-Id") Long userId
    ) {
        cartService.clearCart(userId);
        return ResponseEntity.ok(ApiResponse.success(null));
    }

    @DeleteMapping("/coupon")
    public ResponseEntity<ApiResponse<Void>> removeCoupon(
            @RequestHeader(value = "X-User-Id") Long userId
    ) {
        cartService.removeCoupon(userId);
        return ResponseEntity.ok(ApiResponse.success(null));
    }

}
