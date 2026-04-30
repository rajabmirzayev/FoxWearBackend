package com.foxwear.orderservice.controller;

import com.foxwear.common.dto.ApiResponse;
import com.foxwear.orderservice.dto.request.SaleCreateRequest;
import com.foxwear.orderservice.dto.response.SaleCreateResponse;
import com.foxwear.orderservice.service.SaleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/sellers")
@RequiredArgsConstructor
@Tag(name = "Seller Controller", description = "Sellers management APIs")
public class SellerController {
    private final SaleService saleService;

    @PostMapping
    @Operation(summary = "Create a new sale", description = "Allows a seller to create a new sale record")
    public ResponseEntity<ApiResponse<SaleCreateResponse>> createSale(
            @Valid @RequestBody SaleCreateRequest saleCreateRequest,
            @RequestHeader("X-User-Id") Long sellerId
    ) {
        var response = saleService.createSale(saleCreateRequest, sellerId);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

}
