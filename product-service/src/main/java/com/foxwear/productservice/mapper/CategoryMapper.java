package com.foxwear.productservice.mapper;

import com.foxwear.productservice.dto.request.CategoryRequest;
import com.foxwear.productservice.dto.response.CategoryResponse;
import com.foxwear.productservice.entity.WearCategory;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CategoryMapper {

    WearCategory toEntity(CategoryRequest request);

    CategoryResponse toResponse(WearCategory category);

}
