package com.foxwear.orderservice.mapper;

import com.foxwear.orderservice.dto.response.CartGetResponse;
import com.foxwear.orderservice.entity.Cart;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CartMapper {

    CartGetResponse toGetResponse(Cart cart);

}
