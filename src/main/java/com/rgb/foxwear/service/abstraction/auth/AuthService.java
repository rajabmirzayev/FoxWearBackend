package com.rgb.foxwear.service.abstraction.auth;

import com.rgb.foxwear.dto.request.auth.LoginRequest;
import com.rgb.foxwear.dto.request.auth.RegisterRequest;
import com.rgb.foxwear.dto.response.auth.AuthResponse;

public interface AuthService {
    void register(RegisterRequest registerRequest);
    AuthResponse login(LoginRequest loginRequest);
    AuthResponse refresh(String refreshToken);
}
