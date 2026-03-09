package com.rgb.foxwear.controller.admin;

import com.rgb.foxwear.dto.ApiResponse;
import com.rgb.foxwear.dto.request.catalog.CategoryCreateRequest;
import com.rgb.foxwear.dto.request.catalog.ColorOptionCreateRequest;
import com.rgb.foxwear.dto.request.catalog.ProductCreateRequest;
import com.rgb.foxwear.dto.request.catalog.SizeCreateRequest;
import com.rgb.foxwear.dto.response.catalog.*;
import com.rgb.foxwear.service.abstraction.catalog.ProductService;
import jakarta.validation.Valid;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/products")
@RequiredArgsConstructor
public class AdminProductController {
    private final ProductService productService;

    @PostMapping
    public ResponseEntity<@NonNull ApiResponse<ProductCreateResponse>> createProduct(
            @Valid @RequestBody ProductCreateRequest request
    ) {
        var response = productService.createProduct(request);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @PostMapping("/{productId}/colors")
    public ResponseEntity<@NonNull ApiResponse<ColorOptionCreateResponse>> createColorOption(
            @PathVariable Long productId,
            @Valid @RequestBody ColorOptionCreateRequest request
    ) {
        var response = productService.addColorToProduct(productId, request);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @PostMapping("/category")
    public ResponseEntity<@NonNull ApiResponse<CategoryCreateResponse>> createCategory(
            @Valid @RequestBody CategoryCreateRequest request
    ) {
        var response = productService.createCategory(request);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @PostMapping("/size")
    public ResponseEntity<@NonNull ApiResponse<SizeCreateResponse>> createSize(
            @Valid @RequestBody SizeCreateRequest request
    ) {
        var response = productService.createSize(request);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @PatchMapping("/{id}/activate")
    public ResponseEntity<@NonNull ApiResponse<Void>> updateProductStatus(
            @PathVariable Long id,
            @RequestParam boolean isActive
    ) {
        productService.updateProductActivity(id, isActive);
        return ResponseEntity.ok(ApiResponse.success(null));
    }

    @DeleteMapping("/soft")
    public ResponseEntity<@NonNull ApiResponse<Void>> softDeleteProduct(
            @RequestParam Long id
    ) {
        productService.softDeleteProduct(id);
        return ResponseEntity.ok(ApiResponse.success(null));
    }

    @DeleteMapping
    public ResponseEntity<@NonNull ApiResponse<Void>> deleteProduct(
            @RequestParam Long id
    ) {
        productService.deleteProduct(id);
        return ResponseEntity.ok(ApiResponse.success(null));
    }

    @PatchMapping("/item/{itemId}/stock")
    public ResponseEntity<@NonNull ApiResponse<ItemUpdateResponse>> updateStock(
            @PathVariable Long itemId,
            @RequestParam Integer count
    ) {
        var response = productService.updateStock(itemId, count);

        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @DeleteMapping("/item/soft")
    public ResponseEntity<@NonNull ApiResponse<Void>> softDeleteItem(
            @RequestParam Long id
    ) {
        productService.softDeleteProductItem(id);
        return ResponseEntity.ok(ApiResponse.success(null));
    }

    @DeleteMapping("/item")
    public ResponseEntity<@NonNull ApiResponse<Void>> deleteItem(
            @RequestParam Long id
    ) {
        productService.deleteProductItem(id);
        return ResponseEntity.ok(ApiResponse.success(null));
    }
}
