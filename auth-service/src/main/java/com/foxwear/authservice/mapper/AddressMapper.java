package com.foxwear.authservice.mapper;

import com.foxwear.authservice.dto.request.AddressCreateRequest;
import com.foxwear.authservice.dto.request.AddressUpdateRequest;
import com.foxwear.authservice.dto.response.AddressCreateResponse;
import com.foxwear.authservice.dto.response.AddressGetAllResponse;
import com.foxwear.authservice.dto.response.AddressGetResponse;
import com.foxwear.authservice.dto.response.AddressUpdateResponse;
import com.foxwear.authservice.entity.Address;
import jakarta.validation.Valid;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AddressMapper {

    Address toEntity(AddressCreateRequest request);

    AddressCreateResponse toCreateResponse(Address address);

    AddressUpdateResponse toUpdateResponse(Address address);

    AddressGetResponse toGetResponse(Address address);

    AddressGetAllResponse toGetAllResponse(Address address);
}

