package com.foxwear.interactionservice.mapper;

import com.foxwear.interactionservice.dto.request.ProductReviewCreateRequest;
import com.foxwear.interactionservice.dto.request.SiteReviewCreateRequest;
import com.foxwear.interactionservice.dto.response.*;
import com.foxwear.interactionservice.entity.ProductReview;
import com.foxwear.interactionservice.entity.SiteReview;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ReviewMapper {

    SiteReview toSiteReviewEntity(SiteReviewCreateRequest request);

    ProductReview toProductReviewEntity(ProductReviewCreateRequest request);

    SiteReviewCreateResponse toSiteReviewCreateResponse(SiteReview savedReview);

    ProductReviewCreateResponse toProductReviewCreateResponse(ProductReview savedReview);

    SiteReviewGetAllResponse toSiteReviewGetAllResponse(SiteReview review);

    ProductReviewGetAllResponse toProductReviewGetAllResponse(ProductReview productReview);

    SiteReviewUpdateResponse toSiteReviewUpdateResponse(SiteReview review);

    ProductReviewUpdateResponse toProductReviewUpdateResponse(ProductReview productReview);

}
