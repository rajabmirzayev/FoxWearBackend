package com.foxwear.orderservice.controller;

import com.foxwear.common.dto.ApiResponse;
import com.foxwear.orderservice.dto.response.OrderGetAllResponse;
import com.foxwear.orderservice.dto.response.OrderGetCourierResponse;
import com.foxwear.orderservice.enums.OrderStatus;
import com.foxwear.orderservice.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Courier Order Controller", description = "Endpoints for couriers to manage and pick up orders")
@RestController
@RequestMapping("/api/v1/couriers")
@RequiredArgsConstructor

public class CourierController {
    private final OrderService orderService;

    @Operation(summary = "Get ready orders", description = "Retrieves a list of all orders that are currently in READY_FOR_PICKUP status")
    @GetMapping("/ready")
    public ResponseEntity<ApiResponse<List<OrderGetAllResponse>>> getReadyOrders() {
        var response = orderService.getOrdersByStatus(OrderStatus.READY_FOR_PICKUP);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @Operation(summary = "Get order details for courier", description = "Retrieves specific order details if the courier is assigned to it or if it's available")
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<OrderGetCourierResponse>> getOrder(
            @PathVariable Long id,
            @RequestHeader("X-User-Id") Long courierId
    ) {
        var response = orderService.getOrderByIdWithCourierResponse(id, courierId);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @Operation(summary = "Get courier's active tasks", description = "Retrieves a list of all active orders currently assigned to the authenticated courier")
    @GetMapping
    public ResponseEntity<ApiResponse<List<OrderGetAllResponse>>> getMyActiveTasks(
            @RequestHeader("X-User-Id") Long courierId
    ) {
        var response = orderService.getCourierTasks(courierId);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @Operation(summary = "Get courier's delivery history", description = "Retrieves a paginated list of all orders successfully delivered by the authenticated courier")
    @GetMapping("/my-delivered")
    public ResponseEntity<ApiResponse<Page<OrderGetAllResponse>>> getApiResponse(
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestHeader("X-User-Id") Long courierId
    ) {
        var response = orderService.getMyDeliveredOrders(courierId, page, size);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @Operation(summary = "Assign order to courier", description = "Assigns the current courier to the specified order and updates its status")
    @PatchMapping("/assign/{orderId}")
    public ResponseEntity<ApiResponse<Void>> assignOrder(
            @PathVariable Long orderId,
            @RequestHeader("X-User-Id") Long courierId
    ) {
        orderService.assignCourier(orderId, courierId);
        return ResponseEntity.ok(ApiResponse.success(null));
    }

    @Operation(summary = "Mark order as delivered", description = "Updates the order status to DELIVERED. Only the assigned courier can perform this action.")
    @PatchMapping("/deliver/{orderId}")
    public ResponseEntity<ApiResponse<Void>> markAsDelivered(
            @PathVariable Long orderId,
            @RequestHeader("X-User-Id") Long courierId
    ) {
        orderService.deliverOrder(orderId, courierId);
        return ResponseEntity.ok(ApiResponse.success(null));
    }

}
