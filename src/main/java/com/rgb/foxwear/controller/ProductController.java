package com.rgb.foxwear.controller;

import com.rgb.foxwear.dto.ApiResponse;
import com.rgb.foxwear.dto.response.catalog.ProductGetAllResponse;
import com.rgb.foxwear.service.ProductService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/products")
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;

    @GetMapping("/most-liked")
    public ResponseEntity<@NonNull ApiResponse<List<ProductGetAllResponse>>> getMostLikedProducts() {
        var response = productService.getMostLiked();
        return  ResponseEntity.ok(ApiResponse.success(response));
    }

}
