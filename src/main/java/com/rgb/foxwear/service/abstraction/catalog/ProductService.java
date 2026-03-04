package com.rgb.foxwear.service.abstraction.catalog;

import com.rgb.foxwear.dto.request.catalog.ProductCreateRequest;
import com.rgb.foxwear.dto.response.catalog.ProductCreateResponse;

public interface ProductService {
    ProductCreateResponse createProduct(ProductCreateRequest request);
}
