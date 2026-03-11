package com.rgb.foxwear.controller.admin;

import com.rgb.foxwear.dto.ApiResponse;
import com.rgb.foxwear.dto.request.catalog.*;
import com.rgb.foxwear.dto.response.catalog.*;
import com.rgb.foxwear.service.abstraction.catalog.ProductService;
import org.springframework.data.domain.Page;
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

    @GetMapping
    public ResponseEntity<@NonNull ApiResponse<Page<@NonNull ProductGetAllResponse>>> getAllProduct(
            @Valid @ModelAttribute ProductAdminFilterRequest request
    ) {
        var response = productService.getAllProductWithAdminFilter(request);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @GetMapping("/{id}")
    public ResponseEntity<@NonNull ApiResponse<ProductGetResponse>> getProduct(
            @PathVariable Long id
    ) {
        var response = productService.getProductWithId(id);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @GetMapping("/category/{id}")
    public ResponseEntity<@NonNull ApiResponse<CategoryResponse>> getCategoryById(
            @PathVariable Long id
    ) {
        var response = productService.getCategoryById(id);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @PutMapping("/{id}")
    public ResponseEntity<@NonNull ApiResponse<ProductUpdateResponse>> updateProduct(
            @Valid @RequestBody ProductUpdateRequest request,
            @PathVariable Long id
    ) {
        var response = productService.updateProduct(request, id);
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

    @PatchMapping("/item/{itemId}/stock")
    public ResponseEntity<@NonNull ApiResponse<ItemUpdateResponse>> updateStock(
            @PathVariable Long itemId,
            @RequestParam Integer count
    ) {
        var response = productService.updateStock(itemId, count);

        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @DeleteMapping("/{id}/soft")
    public ResponseEntity<@NonNull ApiResponse<Void>> softDeleteProduct(
            @PathVariable Long id
    ) {
        productService.softDeleteProduct(id);
        return ResponseEntity.ok(ApiResponse.success(null));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<@NonNull ApiResponse<Void>> deleteProduct(
            @PathVariable Long id
    ) {
        productService.deleteProduct(id);
        return ResponseEntity.ok(ApiResponse.success(null));
    }

    @DeleteMapping("/item/{id}/soft")
    public ResponseEntity<@NonNull ApiResponse<Void>> softDeleteItem(
            @PathVariable Long id
    ) {
        productService.softDeleteProductItem(id);
        return ResponseEntity.ok(ApiResponse.success(null));
    }

    @DeleteMapping("/item/{id}")
    public ResponseEntity<@NonNull ApiResponse<Void>> deleteItem(
            @PathVariable Long id
    ) {
        productService.deleteProductItem(id);
        return ResponseEntity.ok(ApiResponse.success(null));
    }
}
