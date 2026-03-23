package com.foxwear.productservice.mapper;

import com.foxwear.productservice.dto.request.ColorOptionDTO;
import com.foxwear.productservice.dto.response.*;
import com.foxwear.productservice.entity.ColorOption;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ColorMapper {

    ColorOption toEntity(ColorOptionDTO colorOptionDTO);

    ColorOptionCreateResponse toCreateResponse(ColorOption color);

    ColorOptionUpdateResponse toUpdateResponse(ColorOption color);

    ColorOptionGetAllResponse toGetAllResponse(ColorOption color);

    ColorOptionAllValuesResponse toGetAllValuesResponse(ColorOption color);

    ColorOptionGetResponse toGetResponse(ColorOption color);

}
