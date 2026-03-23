package com.foxwear.authservice.mapper;

import com.foxwear.authservice.dto.request.CreateAddressRequest;
import com.foxwear.authservice.dto.response.CreateAddressResponse;
import com.foxwear.authservice.entity.Address;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AddressMapper {

    Address toEntity(CreateAddressRequest request);

    CreateAddressResponse toCreateResponse(Address address);

}

