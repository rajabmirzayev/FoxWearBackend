package com.foxwear.orderservice.mapper;

import com.foxwear.orderservice.dto.request.SaleItemCreateRequest;
import com.foxwear.orderservice.dto.response.SaleItemCreateResponse;
import com.foxwear.orderservice.entity.SaleItem;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface SaleItemMapper {

    SaleItem toEntity(SaleItemCreateRequest saleItemCreateRequest);

    SaleItemCreateResponse toCreateResponse(SaleItem saleItem);

}
