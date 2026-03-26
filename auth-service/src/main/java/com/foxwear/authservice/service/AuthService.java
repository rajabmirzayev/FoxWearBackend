package com.foxwear.authservice.service;

import com.foxwear.authservice.dto.request.LoginRequest;
import com.foxwear.authservice.dto.request.PasswordResetRequest;
import com.foxwear.authservice.dto.request.RegisterRequest;
import com.foxwear.authservice.dto.response.AuthResponse;
import com.foxwear.authservice.entity.RefreshToken;
import com.foxwear.authservice.entity.UserEntity;
import com.foxwear.authservice.exception.PasswordMismatchException;
import com.foxwear.authservice.exception.UserAlreadyExistsException;
import com.foxwear.authservice.exception.UserNotFoundException;
import com.foxwear.authservice.mapper.UserMapper;
import com.foxwear.authservice.repository.UserRepository;
import com.foxwear.common.enums.UserStatus;
import com.foxwear.common.exception.InvalidArgumentException;
import com.foxwear.common.exception.InvalidTokenException;
import com.foxwear.common.service.JwtService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

/**
 * Service class for handling authentication operations such as registration, login, and token refreshing.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {
    private final VerificationService verificationService;
    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final UserService userService;
    private final JwtService jwtService;
    private final RefreshTokenService refreshTokenService;
    private final RedisTemplate<String, Object> redisTemplate;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;

    /**
     * Registers a new user after validating input data.
     *
     * @param request the registration details
     */
    @Transactional
    public void register(RegisterRequest request) {
        log.info("Attempting to register user with username: {}", request.getUsername());
        checkUserExists(request);
        checkPasswordsMatch(request.getPassword(), request.getConfirmPassword());

        UserEntity user = userMapper.toEntity(request);
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        var savedUser = userRepository.save(user);
        log.info("User registered successfully with ID: {}", savedUser.getId());

        verificationService.createAndSendVerification(user.getEmail());
    }

    /**
     * Authenticates a user and generates access and refresh tokens.
     *
     * @param loginRequest the login credentials
     * @return AuthResponse containing tokens and user info
     */
    @Transactional
    public AuthResponse login(LoginRequest loginRequest) {
        log.info("Login attempt for username: {}", loginRequest.getUsername());

        UserEntity user = userRepository.findByIdentifier(loginRequest.getUsername())
                .orElseThrow(() -> new UserNotFoundException("Identifier or password is invalid"));

        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            user.getUsername(),
                            loginRequest.getPassword()
                    )
            );
        } catch (BadCredentialsException e) {
            log.warn("Authentication failed for user: {}", loginRequest.getUsername());
            throw new BadCredentialsException("Identifier or password is invalid");
        }

        String token = jwtService.generateToken(user, user.getId());
        RefreshToken refreshToken = refreshTokenService.createRefreshToken(user);

        log.info("User {} logged in successfully", user.getUsername());

        return AuthResponse.builder()
                .accessToken(token)
                .refreshToken(refreshToken.getToken())
                .username(user.getUsername())
                .role(user.getRole())
                .build();
    }

    /**
     * Refreshes the access token using a valid refresh token.
     *
     * @param refreshToken the refresh token string
     * @return AuthResponse with new tokens
     */
    @Transactional
    public AuthResponse refresh(String refreshToken) {
        log.info("Refreshing token");
        RefreshToken verified = refreshTokenService.verifyRefreshToken(refreshToken);

        UserEntity user = verified.getUser();
        String newAccessToken = jwtService.generateToken(user, user.getId());
        RefreshToken newRefreshToken = refreshTokenService.createRefreshToken(user);
        log.info("Token refreshed successfully for user: {}", user.getUsername());

        return AuthResponse.builder()
                .accessToken(newAccessToken)
                .refreshToken(newRefreshToken.getToken())
                .username(user.getUsername())
                .role(user.getRole())
                .build();
    }

    /**
     * Confirms user email using the verification token and activates the account.
     *
     * @param token the verification token from Redis
     * @return redirect URL with generated tokens
     */
    @Transactional
    public String confirm(String token) {
        String email = (String) redisTemplate.opsForValue().get("CONFIRM:" + token);

        if (email == null) {
            throw new InvalidTokenException("Invalid or expired confirmation token");
        }

        UserEntity user = userService.findUserOrThrow(email);

        redisTemplate.delete("CONFIRM:" + token);

        var authorities = user.getAuthorities();
        List<String> roles = authorities.stream()
                .map(GrantedAuthority::getAuthority)
                .toList();

        user.setStatus(UserStatus.ACTIVE);
        log.info("User account activated for email: {}", email);
        String jwtToken = jwtService.generateToken(email, user.getId(), roles);
        String refreshToken = refreshTokenService.createRefreshToken(user).getToken();

        return String.format(
                "http://localhost:3000/auth/callback?token=%s&refreshToken=%s",
                jwtToken, refreshToken
        );
    }

    /**
     * Resets the user's password using a valid reset token.
     *
     * @param request the new password details
     * @param token the password reset token
     */
    @Transactional
    public void resetPassword(PasswordResetRequest request, String token) {
        String email = (String) redisTemplate.opsForValue().get("PWD_RESET:" + token);

        if (email == null) {
            throw new InvalidArgumentException("Invalid or expired token");
        }

        UserEntity user = userService.findUserOrThrow(email);

        redisTemplate.delete("PWD_RESET:" + token);

        checkPasswordsMatch(request.getPassword(), request.getConfirmPassword());

        user.setPassword(passwordEncoder.encode(request.getPassword()));
        log.info("Password successfully reset for user: {}", email);
    }

    /**
     * Checks if the provided password and confirmation password match.
     *
     * @param password the new password
     * @param confirmPassword the confirmation password
     */
    private void checkPasswordsMatch(String password, String confirmPassword) {
        if (!Objects.equals(password, confirmPassword)) {
            throw new PasswordMismatchException("Passwords do not match");
        }
    }

    /**
     * Validates that the username, email, and phone number are unique
     * before proceeding with registration.
     *
     * @param request the registration details
     */
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
}
