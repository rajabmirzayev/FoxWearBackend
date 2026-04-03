package com.foxwear.orderservice.controller;

import com.foxwear.common.dto.ApiResponse;
import com.foxwear.orderservice.dto.response.CouponGetResponse;
import com.foxwear.orderservice.service.CouponService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/coupons")
@RequiredArgsConstructor
public class CouponController {
    private final CouponService couponService;

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<CouponGetResponse>> getCoupon(
            @PathVariable Long id
    ) {
        var response = couponService.getCouponById(id);
        return ResponseEntity.ok(ApiResponse.success(response));
    }
}
