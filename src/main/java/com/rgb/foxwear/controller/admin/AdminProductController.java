package com.rgb.foxwear.controller.admin;

import com.rgb.foxwear.dto.ApiResponse;
import com.rgb.foxwear.dto.request.catalog.*;
import com.rgb.foxwear.dto.response.catalog.*;
import com.rgb.foxwear.service.abstraction.catalog.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import jakarta.validation.Valid;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @Operation(summary = "Add a color option to a product", description = "Adds a new color variant to an existing product.")
    @PostMapping("/{productId}/colors")
    public ResponseEntity<@NonNull ApiResponse<ColorOptionCreateResponse>> createColorOption(
            @Parameter(description = "ID of the product") @PathVariable Long productId,
            @Valid @RequestBody ColorOptionCreateRequest request
    ) {
        var response = productService.addColorToProduct(productId, request);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @Operation(summary = "Create a category", description = "Creates a new product category.")
    @PostMapping("/category")
    public ResponseEntity<@NonNull ApiResponse<CategoryCreateResponse>> createCategory(
            @Valid @RequestBody CategoryRequest request
    ) {
        var response = productService.createCategory(request);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @Operation(summary = "Create a product size", description = "Defines a new size value available for products.")
    @PostMapping("/size")
    public ResponseEntity<@NonNull ApiResponse<SizeCreateResponse>> createSize(
            @Valid @RequestBody SizeCreateRequest request
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

    @Operation(summary = "Get product by ID", description = "Retrieves detailed information for a specific product.")
    @GetMapping("/{id}")
    public ResponseEntity<@NonNull ApiResponse<ProductGetResponse>> getProduct(
            @Parameter(description = "ID of the product") @PathVariable Long id
    ) {
        var response = productService.getProductWithId(id);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @Operation(summary = "Get all categories", description = "Retrieves a list of all product categories.")
    @GetMapping("/category")
    public ResponseEntity<@NonNull ApiResponse<List<CategoryResponse>>> getAllCategories(){
        var response = productService.getAllCategories();
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

    @Operation(summary = "Update product", description = "Updates an existing product's details.")
    @PutMapping("/{id}")
    public ResponseEntity<@NonNull ApiResponse<ProductUpdateResponse>> updateProduct(
            @Valid @RequestBody ProductUpdateRequest request,
            @Parameter(description = "ID of the product") @PathVariable Long id
    ) {
        var response = productService.updateProduct(request, id);
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
            @Parameter(description = "ID of the product") @PathVariable Long id,
            @Parameter(description = "New active status") @RequestParam boolean isActive
    ) {
        productService.updateProductActivity(id, isActive);
        return ResponseEntity.ok(ApiResponse.success(null));
    }

    @Operation(summary = "Update item stock", description = "Updates the stock count for a specific product item (size variant).")
    @PatchMapping("/item/{itemId}/stock")
    public ResponseEntity<@NonNull ApiResponse<ItemUpdateResponse>> updateStock(
            @Parameter(description = "ID of the product item") @PathVariable Long itemId,
            @Parameter(description = "New stock count") @RequestParam Integer count
    ) {
        var response = productService.updateStock(itemId, count);
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

    @Operation(summary = "Soft delete product item", description = "Marks a product item as deleted.")
    @DeleteMapping("/item/{id}/soft")
    public ResponseEntity<@NonNull ApiResponse<Void>> softDeleteItem(
            @Parameter(description = "ID of the product item") @PathVariable Long id
    ) {
        productService.softDeleteProductItem(id);
        return ResponseEntity.ok(ApiResponse.success(null));
    }

    @Operation(summary = "Hard delete product item", description = "Permanently removes a product item from the database.")
    @DeleteMapping("/item/{id}")
    public ResponseEntity<@NonNull ApiResponse<Void>> deleteItem(
            @Parameter(description = "ID of the product item") @PathVariable Long id
    ) {
        productService.deleteProductItem(id);
        return ResponseEntity.ok(ApiResponse.success(null));
    }
}
