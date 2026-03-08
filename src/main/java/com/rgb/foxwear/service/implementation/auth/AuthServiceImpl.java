package com.rgb.foxwear.service.implementation.auth;

import com.rgb.foxwear.dto.request.auth.LoginRequest;
import com.rgb.foxwear.dto.request.auth.RegisterRequest;
import com.rgb.foxwear.dto.response.auth.AuthResponse;
import com.rgb.foxwear.entity.auth.RefreshToken;
import com.rgb.foxwear.entity.auth.UserEntity;
import com.rgb.foxwear.entity.ordering.Cart;
import com.rgb.foxwear.exception.PasswordMismatchException;
import com.rgb.foxwear.exception.UnderageUserException;
import com.rgb.foxwear.exception.UserAlreadyExistsException;
import com.rgb.foxwear.exception.UserNotFoundException;
import com.rgb.foxwear.repository.auth.UserRepository;
import com.rgb.foxwear.service.abstraction.auth.AuthService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Period;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final RefreshTokenService refreshTokenService;
    private final PasswordEncoder passwordEncoder;
    private final ModelMapper mapper;

    @Override
    @Transactional
    public void register(RegisterRequest request) {
        checkUserExists(request);
        checkPasswordsMatch(request);
        checkUnderage(request.getBirthDate());

        UserEntity user = mapper.map(request, UserEntity.class);
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        Cart cart = new Cart();

        user.setCart(cart);

        userRepository.save(user);

    }

    @Override
    @Transactional
    public AuthResponse login(LoginRequest loginRequest) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getUsername(),
                            loginRequest.getPassword()
                    )
            );
        } catch (BadCredentialsException e) {
            throw new BadCredentialsException("Username or password is invalid");
        }

        UserEntity user = userRepository.findByUsername(loginRequest.getUsername())
                .orElseThrow(() -> new UserNotFoundException("Username or password is invalid"));

        String token = jwtService.generateToken(user);
        RefreshToken refreshToken = refreshTokenService.createRefreshToken(user);


        return AuthResponse.builder()
                .accessToken(token)
                .refreshToken(refreshToken.getToken())
                .username(user.getUsername())
                .role(user.getRole())
                .build();
    }

    @Override
    @Transactional
    public AuthResponse refresh(String refreshToken) {
        RefreshToken verified = refreshTokenService.verifyRefreshToken(refreshToken);

        UserEntity user = verified.getUser();
        String newAccessToken = jwtService.generateToken(user);
        RefreshToken newRefreshToken = refreshTokenService.createRefreshToken(user);

        return AuthResponse.builder()
                .accessToken(newAccessToken)
                .refreshToken(newRefreshToken.getToken())
                .username(user.getUsername())
                .role(user.getRole())
                .build();
    }

    private void checkPasswordsMatch(RegisterRequest request) {
        if (!request.getPassword().equals(request.getConfirmPassword())) {
            throw new PasswordMismatchException("Passwords do not match");
        }
    }

    private void checkUserExists(RegisterRequest request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new UserAlreadyExistsException("Username already exists");
        }

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new UserAlreadyExistsException("Email already exists");
        }

        if (userRepository.existsByPhoneNumber(request.getPhoneNumber())) {
            throw new UserAlreadyExistsException("Phone number already exists");
        }
    }

    private void checkUnderage(LocalDate date) {
        if (Period.between(date, LocalDate.now()).getYears() < 18) {
            throw new UnderageUserException("The user must be over 18 years old");
        }
    }
}
