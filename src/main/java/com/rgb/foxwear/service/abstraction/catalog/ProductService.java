package com.rgb.foxwear.service.abstraction.catalog;

import com.rgb.foxwear.dto.request.catalog.CategoryCreateRequest;
import com.rgb.foxwear.dto.request.catalog.ColorOptionCreateRequest;
import com.rgb.foxwear.dto.request.catalog.ProductCreateRequest;
import com.rgb.foxwear.dto.response.catalog.CategoryCreateResponse;
import com.rgb.foxwear.dto.response.catalog.ColorOptionCreateResponse;
import com.rgb.foxwear.dto.response.catalog.ItemUpdateResponse;
import com.rgb.foxwear.dto.response.catalog.ProductCreateResponse;

public interface ProductService {

    ProductCreateResponse createProduct(ProductCreateRequest request);

    ColorOptionCreateResponse addColorToProduct(Long productId, ColorOptionCreateRequest request);

    CategoryCreateResponse createCategory(CategoryCreateRequest request);

    void updateProductActivity(Long id, boolean isActive);

    void softDeleteProduct(Long id);

    void deleteProduct(Long id);

    void softDeleteProductItem(Long id);

    void deleteProductItem(Long id);

    ItemUpdateResponse updateStock(Long itemId, Integer count);

}
