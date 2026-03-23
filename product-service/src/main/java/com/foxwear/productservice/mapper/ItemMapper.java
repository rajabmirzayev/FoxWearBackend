package com.foxwear.productservice.mapper;

import com.foxwear.productservice.dto.response.*;
import com.foxwear.productservice.entity.ProductItem;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ItemMapper {

    ItemUpdateResponse toUpdateResponse(ProductItem productItem);

    ItemCreateResponse toCreateResponse(ProductItem item);

    ItemGetAllResponse toGetAllResponse(ProductItem item);

    ItemGetResponse toGetResponse(ProductItem item);

}
