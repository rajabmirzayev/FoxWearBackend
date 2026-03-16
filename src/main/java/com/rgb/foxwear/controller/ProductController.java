package com.rgb.foxwear.controller;

import com.rgb.foxwear.dto.ApiResponse;
import com.rgb.foxwear.dto.response.catalog.ProductGetAllResponse;
import com.rgb.foxwear.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/products")
@RequiredArgsConstructor
@Tag(name = "Product Management", description = "APIs for product catalog and discovery")
public class ProductController {
    private final ProductService productService;

    @Operation(summary = "Get top 10 most liked products")
    @GetMapping("/most-liked")
    public ResponseEntity<@NonNull ApiResponse<List<ProductGetAllResponse>>> getMostLikedProducts() {
        var response = productService.getMostLiked();
        return  ResponseEntity.ok(ApiResponse.success(response));
    }

}
