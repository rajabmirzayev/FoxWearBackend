package com.rgb.foxwear.service.abstraction.catalog;

import com.rgb.foxwear.dto.request.catalog.ColorOptionCreateRequest;
import com.rgb.foxwear.dto.request.catalog.ProductCreateRequest;
import com.rgb.foxwear.dto.response.catalog.ColorOptionCreateResponse;
import com.rgb.foxwear.dto.response.catalog.ProductCreateResponse;
import jakarta.validation.Valid;

public interface ProductService {
    ProductCreateResponse createProduct(ProductCreateRequest request);

    ColorOptionCreateResponse addColorToProduct(Long productId, ColorOptionCreateRequest request);

    void updateProductActivity(Long id, boolean isActive);

    void softDeleteProduct(Long id);

    void deleteProduct(Long id);
}
