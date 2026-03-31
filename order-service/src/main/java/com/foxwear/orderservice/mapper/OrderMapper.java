package com.foxwear.orderservice.mapper;

import com.foxwear.orderservice.dto.response.OrderCreateResponse;
import com.foxwear.orderservice.entity.Order;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface OrderMapper {

    OrderCreateResponse toCreateResponse(Order order);

}
