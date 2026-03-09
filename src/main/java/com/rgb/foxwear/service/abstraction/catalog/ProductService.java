package com.rgb.foxwear.service.abstraction.catalog;

import com.rgb.foxwear.dto.request.catalog.*;
import com.rgb.foxwear.dto.response.catalog.*;

public interface ProductService {

    ProductCreateResponse createProduct(ProductCreateRequest request);

    ColorOptionCreateResponse addColorToProduct(Long productId, ColorOptionCreateRequest request);

    CategoryCreateResponse createCategory(CategoryCreateRequest request);

    SizeCreateResponse createSize(SizeCreateRequest request);

    ProductUpdateResponse updateProduct(ProductUpdateRequest request, Long id);

    void updateProductActivity(Long id, boolean isActive);

    void softDeleteProduct(Long id);

    void deleteProduct(Long id);

    void softDeleteProductItem(Long id);

    void deleteProductItem(Long id);

    ItemUpdateResponse updateStock(Long itemId, Integer count);

}
