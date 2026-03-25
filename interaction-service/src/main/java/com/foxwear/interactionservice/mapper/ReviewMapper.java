package com.foxwear.interactionservice.mapper;

import com.foxwear.interactionservice.dto.request.SiteReviewCreateRequest;
import com.foxwear.interactionservice.dto.response.SiteReviewGetAllResponse;
import com.foxwear.interactionservice.dto.response.SiteReviewCreateResponse;
import com.foxwear.interactionservice.dto.response.SiteReviewUpdateResponse;
import com.foxwear.interactionservice.entity.SiteReview;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ReviewMapper {

    SiteReview toSiteReviewEntity(SiteReviewCreateRequest request);

    SiteReviewCreateResponse toSiteReviewCreateResponse(SiteReview savedReview);

    SiteReviewGetAllResponse toSiteReviewGetAllResponse(SiteReview review);

    SiteReviewUpdateResponse toSiteReviewUpdateResponse(SiteReview review);

}
