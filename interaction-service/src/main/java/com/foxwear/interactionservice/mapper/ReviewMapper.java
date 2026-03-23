package com.foxwear.interactionservice.mapper;

import com.foxwear.interactionservice.dto.response.ReviewGetAllResponse;
import com.foxwear.interactionservice.entity.SiteReview;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ReviewMapper {
    ReviewGetAllResponse toGetAllResponse(SiteReview r);
}
