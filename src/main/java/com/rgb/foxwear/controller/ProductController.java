package com.rgb.foxwear.controller;

import com.rgb.foxwear.dto.ApiResponse;
import com.rgb.foxwear.dto.request.catalog.ProductCreateRequest;
import com.rgb.foxwear.dto.response.catalog.ProductCreateResponse;
import com.rgb.foxwear.service.abstraction.catalog.ProductService;
import jakarta.validation.Valid;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;

    @PostMapping
    public ResponseEntity<@NonNull ApiResponse<ProductCreateResponse>> createProduct(
            @Valid @RequestBody ProductCreateRequest request
    ) {
        var response = productService.createProduct(request);
        return ResponseEntity.ok(ApiResponse.success(response));
    }
}
