package com.foxwear.orderservice.mapper;

import com.foxwear.orderservice.dto.request.CouponCreateRequest;
import com.foxwear.orderservice.dto.response.CouponCreateResponse;
import com.foxwear.orderservice.dto.response.CouponGetResponse;
import com.foxwear.orderservice.entity.Coupon;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CouponMapper {

    Coupon toEntity(CouponCreateRequest request);

    CouponCreateResponse toCreateResponse(Coupon savedCoupon);

    CouponGetResponse toGetResponse(Coupon coupon);

}
