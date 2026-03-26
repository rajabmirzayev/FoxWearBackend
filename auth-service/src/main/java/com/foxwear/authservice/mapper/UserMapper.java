package com.foxwear.authservice.mapper;

import com.foxwear.authservice.dto.request.RegisterRequest;
import com.foxwear.authservice.dto.response.UserGetPublicResponse;
import com.foxwear.authservice.dto.response.UserGetResponse;
import com.foxwear.authservice.dto.response.UserUpdateResponse;
import com.foxwear.authservice.entity.UserEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserEntity toEntity(RegisterRequest request);

    UserGetResponse toGetResponse(UserEntity user);

    UserUpdateResponse toUpdateResponse(UserEntity user);

    UserGetPublicResponse toGetPublicResponse(UserEntity user);
}
