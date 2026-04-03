package com.foxwear.orderservice.controller.admin;

import com.foxwear.common.dto.ApiResponse;
import com.foxwear.orderservice.dto.request.CouponCreateRequest;
import com.foxwear.orderservice.dto.response.CouponCreateResponse;
import com.foxwear.orderservice.service.CouponService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/coupons")
@RequiredArgsConstructor
public class AdminCouponController {
    private final CouponService couponService;

    @PostMapping
    public ResponseEntity<ApiResponse<CouponCreateResponse>> createCoupon(
            @Valid @RequestBody CouponCreateRequest couponCreateRequest
    ) {
        var response = couponService.createCoupon(couponCreateRequest);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @PatchMapping("/{id}/activate")
    public ResponseEntity<ApiResponse<String>> updateCoupon(
            @PathVariable Long id
    ) {
        var response = couponService.toggleActivate(id);
        return ResponseEntity.ok(ApiResponse.success(response ? "Activated" : "Deactivated"));
    }

}
