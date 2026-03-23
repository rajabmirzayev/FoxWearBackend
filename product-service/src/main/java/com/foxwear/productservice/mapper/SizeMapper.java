package com.foxwear.productservice.mapper;

import com.foxwear.productservice.dto.request.SizeRequest;
import com.foxwear.productservice.dto.response.SizeResponse;
import com.foxwear.productservice.entity.ProductSize;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface SizeMapper {

    ProductSize toEntity(SizeRequest request);

    SizeResponse toResponse(ProductSize savedSize);

}
