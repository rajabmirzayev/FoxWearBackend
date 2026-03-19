package com.rgb.foxwear.controller.admin;

import com.rgb.foxwear.dto.ApiResponse;
import com.rgb.foxwear.dto.request.catalog.*;
import com.rgb.foxwear.dto.response.catalog.*;
import com.rgb.foxwear.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import jakarta.validation.Valid;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/products")
@RequiredArgsConstructor
@Tag(name = "Admin Product Management", description = "Endpoints for administrators to manage products, categories, sizes, and stock.")
public class AdminProductController {
    private final ProductService productService;

    @Operation(summary = "Create a new product", description = "Creates a product with its associated colors, images, and items.")
    @PostMapping
    public ResponseEntity<@NonNull ApiResponse<ProductCreateResponse>> createProduct(
            @Valid @RequestBody ProductCreateRequest request
    ) {
        var response = productService.createProduct(request);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @Operation(summary = "Create a category", description = "Creates a new product category.")
    @PostMapping("/category")
    public ResponseEntity<@NonNull ApiResponse<CategoryResponse>> createCategory(
            @Valid @RequestBody CategoryRequest request
    ) {
        var response = productService.createCategory(request);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @Operation(summary = "Create a product size", description = "Defines a new size value available for products.")
    @PostMapping("/size")
    public ResponseEntity<@NonNull ApiResponse<SizeResponse>> createSize(
            @Valid @RequestBody SizeRequest request
    ) {
        var response = productService.createSize(request);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @Operation(summary = "Get all products (Admin Filter)", description = "Retrieves a paginated list of products with admin-specific filters.")
    @GetMapping
    public ResponseEntity<@NonNull ApiResponse<Page<@NonNull ProductGetAllResponse>>> getAllProduct(
            @Valid @ModelAttribute ProductAdminFilterRequest request
    ) {
        var response = productService.getAllProductWithAdminFilter(request);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @Operation(summary = "Get category by ID", description = "Retrieves a specific category by its ID.")
    @GetMapping("/category/{id}")
    public ResponseEntity<@NonNull ApiResponse<CategoryResponse>> getCategoryById(
            @Parameter(description = "ID of the category") @PathVariable Long id
    ) {
        var response = productService.getCategoryById(id);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @Operation(summary = "Get size by ID", description = "Retrieves a specific size definition by its ID.")
    @GetMapping("/size/{id}")
    public ResponseEntity<@NonNull ApiResponse<SizeResponse>> getSizeById(
            @Parameter(description = "ID of the size") @PathVariable Long id
    ) {
        var response = productService.getSizeById(id);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @Operation(summary = "Update product", description = "Updates an existing product's details.")
    @PutMapping("/{id}")
    public ResponseEntity<@NonNull ApiResponse<ProductUpdateResponse>> updateProduct(
            @Valid @RequestBody ProductUpdateRequest request,
            @Parameter(description = "ID of the product") @PathVariable Long id
    ) {
        var response = productService.updateProduct(request, id);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @Operation(summary = "Update size", description = "Updates an existing size definition.")
    @PutMapping("/size/{id}")
    public ResponseEntity<@NonNull ApiResponse<SizeResponse>> updateSize(
            @Parameter(description = "ID of the size") @PathVariable Long id,
            @Valid @RequestBody SizeRequest request
    ) {
        var response = productService.updateSize(request, id);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @Operation(summary = "Update category", description = "Updates an existing category's details.")
    @PutMapping("/category/{id}")
    public ResponseEntity<@NonNull ApiResponse<CategoryResponse>> updateCategory(
            @Valid @RequestBody CategoryRequest request,
            @Parameter(description = "ID of the category") @PathVariable Long id
    ) {
        var response = productService.updateCategory(request, id);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @Operation(summary = "Update product activity status", description = "Activates or deactivates a product.")
    @PatchMapping("/{id}/activate")
    public ResponseEntity<@NonNull ApiResponse<Void>> updateProductStatus(
            @Parameter(description = "ID of the product") @PathVariable Long id
    ) {
        productService.updateProductActivity(id);
        return ResponseEntity.ok(ApiResponse.success(null));
    }

    @Operation(summary = "Update item stock", description = "Updates the stock count for a specific product item (size variant).")
    @PatchMapping("/item/{id}/stock")
    public ResponseEntity<@NonNull ApiResponse<ItemUpdateResponse>> updateStock(
            @Parameter(description = "ID of the product item") @PathVariable Long id,
            @Parameter(description = "New stock count") @RequestParam Integer count
    ) {
        var response = productService.updateStock(id, count);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @Operation(summary = "Soft delete product", description = "Marks a product as deleted without removing it from the database.")
    @DeleteMapping("/{id}/soft")
    public ResponseEntity<@NonNull ApiResponse<Void>> softDeleteProduct(
            @Parameter(description = "ID of the product") @PathVariable Long id
    ) {
        productService.softDeleteProduct(id);
        return ResponseEntity.ok(ApiResponse.success(null));
    }

    @Operation(summary = "Hard delete product", description = "Permanently removes a product from the database.")
    @DeleteMapping("/{id}")
    public ResponseEntity<@NonNull ApiResponse<Void>> deleteProduct(
            @Parameter(description = "ID of the product") @PathVariable Long id
    ) {
        productService.deleteProduct(id);
        return ResponseEntity.ok(ApiResponse.success(null));
    }

    @Operation(summary = "Delete size", description = "Permanently removes a size definition from the database.")
    @DeleteMapping("/size/{id}")
    public ResponseEntity<@NonNull ApiResponse<Void>> deleteSize(
            @Parameter(description = "ID of the size") @PathVariable Long id
    ) {
        productService.deleteSize(id);
        return ResponseEntity.ok(ApiResponse.success(null));
    }

    @Operation(summary = "Delete category", description = "Permanently deletes a category and its products from the database.")
    @DeleteMapping("/category/{id}")
    public ResponseEntity<@NonNull ApiResponse<Void>> deleteCategory(
            @Parameter(description = "ID of the category") @PathVariable Long id
    ) {
        productService.deleteCategory(id);
        return ResponseEntity.ok(ApiResponse.success(null));
    }
}