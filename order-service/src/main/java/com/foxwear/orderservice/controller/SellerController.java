package com.foxwear.orderservice.controller;

import com.foxwear.common.dto.ApiResponse;
import com.foxwear.orderservice.dto.request.SaleCreateRequest;
import com.foxwear.orderservice.dto.response.OrderGetAllResponse;
import com.foxwear.orderservice.dto.response.SaleCreateResponse;
import com.foxwear.orderservice.enums.OrderStatus;
import com.foxwear.orderservice.service.OrderService;
import com.foxwear.orderservice.service.SaleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/sellers")
@RequiredArgsConstructor
@Tag(name = "Seller Controller", description = "Sellers management APIs")
public class SellerController {
    private final SaleService saleService;
    private final OrderService orderService;

    @PostMapping
    @Operation(summary = "Create a new sale", description = "Allows a seller to create a new sale record")
    public ResponseEntity<ApiResponse<SaleCreateResponse>> createSale(
            @Valid @RequestBody SaleCreateRequest saleCreateRequest,
            @RequestHeader("X-User-Id") Long sellerId
    ) {
        var response = saleService.createSale(saleCreateRequest, sellerId);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @Operation(summary = "Get pending orders", description = "Retrieves a list of all orders that are currently in PENDING status")
    @GetMapping("/pending")
    public ResponseEntity<ApiResponse<List<OrderGetAllResponse>>> getPendingOrders() {
        var response = orderService.getOrdersByStatus(OrderStatus.PENDING);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @Operation(summary = "Get preparing orders", description = "Retrieves a list of all orders that are currently in PREPARING status")
    @GetMapping("/preparing")
    public ResponseEntity<ApiResponse<List<OrderGetAllResponse>>> getPreparingOrders() {
        var response = orderService.getOrdersByStatus(OrderStatus.PREPARING);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @Operation(summary = "Get prepared orders", description = "Retrieves a list of all orders that are currently in READY_FOR_PICKUP status")
    @GetMapping("/prepared")
    public ResponseEntity<ApiResponse<List<OrderGetAllResponse>>> getPreparedOrders() {
        var response = orderService.getOrdersByStatus(OrderStatus.READY_FOR_PICKUP);
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

}
