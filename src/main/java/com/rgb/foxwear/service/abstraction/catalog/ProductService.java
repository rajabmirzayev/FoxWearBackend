package com.rgb.foxwear.service.abstraction.catalog;

import com.rgb.foxwear.dto.request.catalog.*;
import com.rgb.foxwear.dto.response.catalog.*;
import lombok.NonNull;
import org.springframework.data.domain.Page;

import java.util.List;

public interface ProductService {

    ProductCreateResponse createProduct(ProductCreateRequest request);

    ColorOptionCreateResponse addColorToProduct(Long productId, ColorOptionCreateRequest request);

    CategoryResponse createCategory(CategoryRequest request);

    SizeResponse createSize(SizeRequest request);

    Page<@NonNull ProductGetAllResponse> getAllProductWithAdminFilter(ProductAdminFilterRequest filter);

    ProductGetResponse getProductWithId(Long id);

    List<ColorOptionAllValuesResponse> getAllColorOptionsValues();

    ItemGetResponse getItemById(Long id);

    List<CategoryResponse> getAllCategories();

    CategoryResponse getCategoryById(Long id);

    List<SizeResponse> getAllSizes();

    SizeResponse getSizeById(Long id);

    ProductUpdateResponse updateProduct(ProductUpdateRequest request, Long id);

    CategoryResponse updateCategory(CategoryRequest request, Long id);

    SizeResponse updateSize(SizeRequest request, Long id);

    ItemUpdateResponse updateStock(Long itemId, Integer count);

    void updateProductActivity(Long id);

    void softDeleteProduct(Long id);

    void deleteProduct(Long id);

    void softDeleteProductItem(Long id);

    void deleteProductItem(Long id);

    void deleteSize(Long id);

}
