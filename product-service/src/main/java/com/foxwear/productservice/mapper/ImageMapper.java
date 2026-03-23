package com.foxwear.productservice.mapper;

import com.foxwear.productservice.dto.request.ImageDTO;
import com.foxwear.productservice.dto.response.ImageCreateResponse;
import com.foxwear.productservice.dto.response.ImageGetAllResponse;
import com.foxwear.productservice.dto.response.ImageGetResponse;
import com.foxwear.productservice.dto.response.ImageUpdateResponse;
import com.foxwear.productservice.entity.ColorOptionImage;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ImageMapper {

    ColorOptionImage toEntity(ImageDTO image);

    ImageCreateResponse toCreateResponse(ColorOptionImage image);

    ImageUpdateResponse toUpdateResponse(ColorOptionImage image);

    ImageGetAllResponse toGetAllResponse(ColorOptionImage image);

    ImageGetResponse toGetResponse(ColorOptionImage image);

}
