package com.rgb.foxwear.service.abstraction.catalog;

import com.rgb.foxwear.dto.request.catalog.*;
import com.rgb.foxwear.dto.response.catalog.*;
import lombok.NonNull;
import org.springframework.data.domain.Page;

public interface ProductService {

    ProductCreateResponse createProduct(ProductCreateRequest request);

    ColorOptionCreateResponse addColorToProduct(Long productId, ColorOptionCreateRequest request);

    CategoryCreateResponse createCategory(CategoryCreateRequest request);

    SizeCreateResponse createSize(SizeCreateRequest request);

    Page<@NonNull ProductGetAllResponse> getAllProductWithAdminFilter(ProductAdminFilterRequest filter);

    ProductGetResponse getProductWithId(Long id);

    ProductUpdateResponse updateProduct(ProductUpdateRequest request, Long id);

    void updateProductActivity(Long id, boolean isActive);

    void softDeleteProduct(Long id);

    void deleteProduct(Long id);

    void softDeleteProductItem(Long id);

    void deleteProductItem(Long id);

    ItemUpdateResponse updateStock(Long itemId, Integer count);

}
