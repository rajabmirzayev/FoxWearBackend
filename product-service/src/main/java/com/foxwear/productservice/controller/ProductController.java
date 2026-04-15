package com.foxwear.productservice.controller;

import com.foxwear.common.dto.ApiResponse;
import com.foxwear.common.dto.response.ProductResponse;
import com.foxwear.productservice.dto.request.ProductUserFilterRequest;
import com.foxwear.productservice.dto.response.*;
import com.foxwear.productservice.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/v1/products")
@RequiredArgsConstructor
@Tag(name = "Product Management", description = "APIs for product catalog and discovery")
public class ProductController {
    private final ProductService productService;

    @Operation(summary = "Get product by slug", description = "Retrieves detailed information for a specific product.")
    @GetMapping("/{slug}")
    public ResponseEntity<ApiResponse<ProductGetResponse>> getProduct(
            @Parameter(description = "Slug of the product") @PathVariable String slug,
            @RequestHeader(value = "X-User-Id") Long userId
    ) {
        var response = productService.getProductWithSlug(slug, userId);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @GetMapping("/basic/{itemId}")
    public ResponseEntity<ApiResponse<ProductResponse>> getProduct(
            @Parameter(description = "ID of the product item") @PathVariable Long itemId
    ) {
        var response = productService.getProductWithItemId(itemId);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @Operation(summary = "Get all products (User Filter)", description = "Retrieves a paginated list of products based on user-defined filters like category, color, size, and price.")
    @GetMapping
    public ResponseEntity<ApiResponse<Page<ProductGetAllResponse>>> getAllProducts(
            @Valid @ModelAttribute ProductUserFilterRequest request,
            @RequestHeader(value = "X-User-Id", required = false) Long userId
    ) {
        var response = productService.getAllProductWithUserFilter(request, userId);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @Operation(summary = "Get top 10 most liked products", description = "Retrieves a list of the top 10 products with the highest number of user likes.")
    @GetMapping("/most-liked")
    public ResponseEntity<ApiResponse<List<ProductGetAllResponse>>> getMostLikedProducts(
            @RequestHeader(value = "X-User-Id", required = false) Long userId
    ) {
        var response = productService.getMostLiked(userId);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @Operation(summary = "Get my liked products", description = "Retrieves a list of products that the authenticated user has liked.")
    @GetMapping("/my-liked-products")
    public ResponseEntity<ApiResponse<List<ProductGetAllResponse>>> getMyLikedProducts(
            @RequestHeader(value = "X-User-Id") Long userId
    ) {
        var response = productService.getMyLikedProducts(userId);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @Operation(summary = "Get all color option values", description = "Retrieves a list of all unique color names and codes used across products.")
    @GetMapping("/colors")
    public ResponseEntity<ApiResponse<List<ColorOptionAllValuesResponse>>> getAllColorOptionsValues() {
        var response = productService.getAllColorOptionsValues();
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @Operation(summary = "Get all categories", description = "Retrieves a list of all product categories.")
    @GetMapping("/category")
    public ResponseEntity<ApiResponse<List<CategoryGetAllResponse>>> getAllCategories() {
        var response = productService.getAllCategories();
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @GetMapping("/category/{id}")
    public ResponseEntity<ApiResponse<CategoryResponse>> getCategoryById(
            @Parameter(description = "ID of the category") @PathVariable Long id
    ) {
        var response = productService.getCategoryById(id);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @Operation(summary = "Get all sizes", description = "Retrieves a list of all defined product sizes.")
    @GetMapping("/size")
    public ResponseEntity<ApiResponse<List<SizeResponse>>> getAllSizes() {
        var response = productService.getAllSizes();
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @GetMapping("/{id}/price")
    public ResponseEntity<ApiResponse<BigDecimal>> getProductPrice(
            @Parameter(description = "ID of the product item") @PathVariable Long id
    ) {
        var response = productService.getProductPrice(id);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

}
