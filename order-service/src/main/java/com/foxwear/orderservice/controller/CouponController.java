package com.foxwear.orderservice.controller;

import com.foxwear.common.dto.ApiResponse;
import com.foxwear.orderservice.dto.response.CouponGetResponse;
import com.foxwear.orderservice.service.CouponService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/coupons")
@RequiredArgsConstructor
@Tag(name = "Coupon Controller", description = "Endpoints for managing and retrieving coupons")
public class CouponController {
    private final CouponService couponService;

    @Operation(summary = "Get coupon by ID", description = "Returns coupon details based on the provided unique identifier")
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<CouponGetResponse>> getCoupon(
            @Parameter(description = "Unique identifier of the coupon", required = true) @PathVariable Long id
    ) {
        var response = couponService.getCouponById(id);
        return ResponseEntity.ok(ApiResponse.success(response));
    }
}
