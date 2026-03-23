package com.foxwear.dynamicdataservice.mapper;

import com.foxwear.dynamicdataservice.dto.request.BannerRequest;
import com.foxwear.dynamicdataservice.dto.response.BannerResponse;
import com.foxwear.dynamicdataservice.entity.Banner;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface BannerMapper {

    Banner toEntity(BannerRequest request);

    BannerResponse toResponse(Banner banner);

}
