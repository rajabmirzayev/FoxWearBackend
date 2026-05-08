package com.foxwear.orderservice.mapper;

import com.foxwear.orderservice.dto.request.SaleCreateRequest;
import com.foxwear.orderservice.dto.response.SaleCreateResponse;
import com.foxwear.orderservice.dto.response.SaleGetAllResponse;
import com.foxwear.orderservice.dto.response.SaleGetResponse;
import com.foxwear.orderservice.entity.Sale;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface SaleMapper {

    Sale toEntity(SaleCreateRequest saleCreateRequest);

    SaleCreateResponse toCreateResponse(Sale sale);

    SaleGetAllResponse toGetAllResponse(Sale sale);

    SaleGetResponse toGetResponse(Sale sale);

}
