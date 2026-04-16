package com.foxwear.productservice.mapper;

import com.foxwear.common.dto.response.ProductResponse;
import com.foxwear.productservice.dto.request.ProductCreateRequest;
import com.foxwear.productservice.dto.response.ProductCreateResponse;
import com.foxwear.productservice.dto.response.ProductGetAllResponse;
import com.foxwear.productservice.dto.response.ProductGetResponse;
import com.foxwear.productservice.dto.response.ProductUpdateResponse;
import com.foxwear.productservice.entity.Product;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ProductMapper {

    Product toEntity(ProductCreateRequest request);

    ProductGetResponse toGetResponse(Product product);

    ProductResponse toResponse(Product product);

    ProductCreateResponse toCreateResponse(Product product);

    ProductUpdateResponse toUpdateResponse(Product product);

    ProductGetAllResponse toGetAllResponse(Product product);

}
