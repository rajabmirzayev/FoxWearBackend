package com.foxwear.orderservice.controller;

import com.foxwear.common.dto.ApiResponse;
import com.foxwear.orderservice.dto.request.OrderCreateRequest;
import com.foxwear.orderservice.dto.response.OrderCreateResponse;
import com.foxwear.orderservice.dto.response.OrderGetAllResponse;
import com.foxwear.orderservice.dto.response.OrderGetResponse;
import com.foxwear.orderservice.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/orders")
@RequiredArgsConstructor
@Tag(name = "Order Controller", description = "Endpoints for managing customer orders")
public class OrderController {

    private final OrderService orderService;

    @Operation(summary = "Create a new order", description = "Processes a new order request and returns the created order details")
    @PostMapping
    public ResponseEntity<ApiResponse<OrderCreateResponse>> createOrder(
            @Valid @RequestBody OrderCreateRequest request,
            @Parameter(description = "ID of the user placing the order") @RequestHeader(value = "X-User-Id") Long userId
    ) {
        OrderCreateResponse response = orderService.createOrder(request, userId);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(response));
    }

    @Operation(summary = "Get my orders", description = "Retrieves a list of all orders belonging to the authenticated user")
    @GetMapping("/my")
    public ResponseEntity<ApiResponse<List<OrderGetAllResponse>>> getMyOrders(
            @Parameter(description = "ID of the user whose orders are being retrieved") @RequestHeader("X-User-Id") Long userId
    ) {
        var response = orderService.getMyOrders(userId);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @Operation(summary = "Get order details", description = "Retrieves details of a specific order by its order number for the authenticated user")
    @GetMapping("/{orderNumber}")
    public ResponseEntity<ApiResponse<OrderGetResponse>> getOrderDetails(
            @Parameter(description = "The unique order number") @PathVariable String orderNumber,
            @RequestHeader("X-User-Id") Long userId
    ) {
        OrderGetResponse response = orderService.getOrderByOrderNumber(orderNumber, userId);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @GetMapping("/data/{id}")
    public ResponseEntity<ApiResponse<OrderGetResponse>> getOrderDetails(
            @PathVariable Long id,
            @RequestHeader("X-User-Id") Long userId
    ) {
        OrderGetResponse response = orderService.getOrderById(id, userId);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

}