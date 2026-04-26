package com.foxwear.orderservice.controller;

import com.foxwear.common.dto.ApiResponse;
import com.foxwear.orderservice.dto.request.OrderCreateRequest;
import com.foxwear.orderservice.dto.response.OrderCreateResponse;
import com.foxwear.orderservice.dto.response.OrderGetAllResponse;
import com.foxwear.orderservice.dto.response.OrderGetResponse;
import com.foxwear.orderservice.enums.OrderStatus;
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

    @Operation(summary = "Get pending orders", description = "Retrieves a list of all orders that are currently in PENDING status")
    @GetMapping("/pending")
    public ResponseEntity<ApiResponse<List<OrderGetAllResponse>>> getPendingOrders() {
        var response = orderService.getOrdersByStatus(OrderStatus.PENDING);
        return ResponseEntity.ok(ApiResponse.success(response));
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


    @Operation(summary = "Set order to preparing", description = "Updates the order status to PREPARING. Requires admin privileges.")
    @PatchMapping("/preparing/{orderId}")
    public ResponseEntity<ApiResponse<Void>> setPreparingOrder(
            @Parameter(description = "ID of the order to update") @PathVariable Long orderId,
            @RequestHeader("X-User-Id") Long adminId) {
        orderService.setPreparingOrder(orderId, adminId);
        return ResponseEntity.ok(ApiResponse.success(null));
    }

    @Operation(summary = "Set order to prepared", description = "Updates the order status to PREPARED. Requires admin privileges.")
    @PatchMapping("/prepared/{orderId}")
    public ResponseEntity<ApiResponse<Void>> setPreparedOrder(
            @Parameter(description = "ID of the order to update") @PathVariable Long orderId,
            @RequestHeader("X-User-Id") Long adminId) {
        orderService.setPreparedOrder(orderId, adminId);
        return ResponseEntity.ok(ApiResponse.success(null));
    }

    @Operation(summary = "Assign courier to order", description = "Assigns a courier to the specified order and updates status to ON_THE_WAY")
    @PatchMapping("/assign/{orderId}")
    public ResponseEntity<ApiResponse<Void>> assignCourier(
            @Parameter(description = "ID of the order to assign") @PathVariable Long orderId,
            @RequestHeader("X-User-Id") Long courierId) {
        orderService.assignCourier(orderId, courierId);
        return ResponseEntity.ok(ApiResponse.success(null));
    }

    @Operation(summary = "Mark order as delivered", description = "Updates the order status to DELIVERED")
    @PatchMapping("/deliver/{orderId}")
    public ResponseEntity<ApiResponse<Void>> deliverOrder(
            @Parameter(description = "ID of the order to mark as delivered") @PathVariable Long orderId,
            @RequestHeader("X-User-Id") Long courierId
    ) {
        orderService.deliverOrder(orderId, courierId);
        return ResponseEntity.ok(ApiResponse.success(null));
    }

}