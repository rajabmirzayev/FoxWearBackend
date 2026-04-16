package com.foxwear.orderservice.mapper;

import com.foxwear.orderservice.dto.response.OrderItemCreateResponse;
import com.foxwear.orderservice.dto.response.OrderItemGetResponse;
import com.foxwear.orderservice.entity.OrderItem;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface OrderItemMapper {

    OrderItemCreateResponse toCreateResponse(OrderItem orderItem);

    OrderItemGetResponse toGetResponse(OrderItem orderItem);

}
