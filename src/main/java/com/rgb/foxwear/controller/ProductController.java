package com.rgb.foxwear.controller;

import com.rgb.foxwear.dto.ApiResponse;
import com.rgb.foxwear.dto.request.catalog.ProductUserFilterRequest;
import com.rgb.foxwear.dto.response.catalog.CategoryResponse;
import com.rgb.foxwear.dto.response.catalog.ColorOptionAllValuesResponse;
import com.rgb.foxwear.dto.response.catalog.ProductGetAllResponse;
import com.rgb.foxwear.dto.response.catalog.SizeResponse;
import com.rgb.foxwear.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/products")
@RequiredArgsConstructor
@Tag(name = "Product Management", description = "APIs for product catalog and discovery")
public class ProductController {
    private final ProductService productService;

    @Operation(summary = "Get all products (User Filter)", description = "Retrieves a paginated list of products based on user-defined filters like category, color, size, and price.")
    @GetMapping
    public ResponseEntity<@NonNull ApiResponse<Page<@NonNull ProductGetAllResponse>>> getAllProducts(
            @Valid @ModelAttribute ProductUserFilterRequest request
    ) {
        var response = productService.getAllProductWithUserFilter(request);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @Operation(summary = "Get top 10 most liked products", description = "Retrieves a list of the top 10 products with the highest number of user likes.")
    @GetMapping("/most-liked")
    public ResponseEntity<@NonNull ApiResponse<List<ProductGetAllResponse>>> getMostLikedProducts() {
        var response = productService.getMostLiked();
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @Operation(summary = "Get all color option values", description = "Retrieves a list of all unique color names and codes used across products.")
    @GetMapping("/colors")
    public ResponseEntity<@NonNull ApiResponse<List<ColorOptionAllValuesResponse>>> getAllColorOptionsValues() {
        var response = productService.getAllColorOptionsValues();
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @Operation(summary = "Get all categories", description = "Retrieves a list of all product categories.")
    @GetMapping("/category")
    public ResponseEntity<@NonNull ApiResponse<List<CategoryResponse>>> getAllCategories() {
        var response = productService.getAllCategories();
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @Operation(summary = "Get all sizes", description = "Retrieves a list of all defined product sizes.")
    @GetMapping("/size")
    public ResponseEntity<@NonNull ApiResponse<List<SizeResponse>>> getAllSizes() {
        var response = productService.getAllSizes();
        return ResponseEntity.ok(ApiResponse.success(response));
    }

}
