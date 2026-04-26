package com.foxwear.orderservice.controller.admin;

import com.foxwear.common.dto.ApiResponse;
import com.foxwear.orderservice.dto.request.OrderFilterRequest;
import com.foxwear.orderservice.dto.response.OrderGetAllResponse;
import com.foxwear.orderservice.dto.response.OrderGetResponse;
import com.foxwear.orderservice.enums.OrderStatus;
import com.foxwear.orderservice.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/orders")
@RequiredArgsConstructor
@Tag(name = "Admin Order Management", description = "Endpoints for administrators to manage and view orders")
public class AdminOrderController {
    private final OrderService orderService;

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<OrderGetResponse>> getOrderDetails(
            @PathVariable Long id,
            @RequestHeader(value = "X-User-Id") Long userId
    ) {
        var response = orderService.getOrderById(id, userId);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @GetMapping
    @Operation(summary = "Get all orders", description = "Retrieve a paginated list of orders with optional filtering")
    public ResponseEntity<ApiResponse<Page<OrderGetAllResponse>>> getOrders(
            @ModelAttribute OrderFilterRequest request
    ) {
        var response = orderService.getAllOrders(request);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @GetMapping("/order-status")
    @Operation(summary = "Get order statuses", description = "Retrieve a list of all possible order statuses")
    public ResponseEntity<ApiResponse<List<String>>> getOrderStatuses() {
        var response = orderService.getOrderStatuses();
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @GetMapping("/payment-status")
    @Operation(summary = "Get payment statuses", description = "Retrieve a list of all possible payment statuses")
    public ResponseEntity<ApiResponse<List<String>>> getPaymentStatuses() {
        var response = orderService.getPaymentStatuses();
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @GetMapping("/payment-method")
    @Operation(summary = "Get payment methods", description = "Retrieve a list of all supported payment methods")
    public ResponseEntity<ApiResponse<List<String>>> getPaymentMethods() {
        var response = orderService.getPaymentMethods();
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @PatchMapping("/{orderId}/status")
    @Operation(summary = "Update order status", description = "Update the status of a specific order by its ID")
    public ResponseEntity<ApiResponse<Void>> updateOrderStatus(
            @PathVariable Long orderId,
            @RequestParam OrderStatus status
    ) {
        orderService.updateOrderStatus(orderId, status);
        return ResponseEntity.ok(ApiResponse.success(null));
    }

}
