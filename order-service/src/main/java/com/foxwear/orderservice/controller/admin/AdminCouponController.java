package com.foxwear.orderservice.controller.admin;

import com.foxwear.common.dto.ApiResponse;
import com.foxwear.orderservice.dto.request.CouponCreateRequest;
import com.foxwear.orderservice.dto.response.CouponCreateResponse;
import com.foxwear.orderservice.service.CouponService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/coupons")
@RequiredArgsConstructor
@Tag(name = "Admin Coupon Management", description = "Endpoints for administrators to manage discount coupons")
public class AdminCouponController {
    private final CouponService couponService;

    @PostMapping
    @Operation(summary = "Create a new coupon", description = "Creates a new discount coupon with specified constraints")
    public ResponseEntity<ApiResponse<CouponCreateResponse>> createCoupon(
            @Valid @RequestBody CouponCreateRequest couponCreateRequest
    ) {
        var response = couponService.createCoupon(couponCreateRequest);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @PatchMapping("/{id}/activate")
    @Operation(summary = "Toggle coupon activation status", description = "Activates or deactivates a coupon by its ID")
    public ResponseEntity<ApiResponse<String>> updateCoupon(
            @PathVariable Long id
    ) {
        var response = couponService.toggleActivate(id);
        return ResponseEntity.ok(ApiResponse.success(response ? "Activated" : "Deactivated"));
    }
}
