package com.rgb.foxwear.service.abstraction.auth;

import com.rgb.foxwear.dto.request.auth.CreateUserRequest;
import com.rgb.foxwear.dto.response.auth.CreateUserResponse;

public interface UserService {
    CreateUserResponse createUser(CreateUserRequest request);
}
