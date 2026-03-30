package com.foxwear.orderservice.mapper;

import com.foxwear.orderservice.dto.request.CartItemCreateRequest;
import com.foxwear.orderservice.dto.response.CartItemCreateResponse;
import com.foxwear.orderservice.dto.response.CartItemGetResponse;
import com.foxwear.orderservice.dto.response.CartItemUpdateResponse;
import com.foxwear.orderservice.entity.CartItem;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CartItemMapper {

    CartItemGetResponse toGetResponse(CartItem cartItem);

    CartItem toEntity(CartItemCreateRequest request);

    CartItemCreateResponse toCreateResponse(CartItem savedItem);

    CartItemUpdateResponse toUpdateResponse(CartItem cartItem);

}
