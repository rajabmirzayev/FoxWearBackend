package com.foxwear.authservice.service;

import com.foxwear.authservice.entity.RefreshToken;
import com.foxwear.authservice.entity.UserEntity;
import com.foxwear.authservice.repository.RefreshTokenRepository;
import com.foxwear.common.exception.InvalidTokenException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {

    @Value("${jwt.refresh-expiration}")
    private long refreshExpiration;

    private final RefreshTokenRepository refreshTokenRepository;

    /**
     * Creates a new refresh token for a user, removing any existing tokens first.
     *
     * @param user The user entity for whom the token is created
     * @return The saved RefreshToken entity
     */
    public RefreshToken createRefreshToken(UserEntity user) {
        refreshTokenRepository.deleteByUser(user);

        RefreshToken refreshToken = RefreshToken.builder()
                .token(UUID.randomUUID().toString())
                .user(user)
                .expiresAt(LocalDateTime.now().plusSeconds(refreshExpiration / 1000))
                .revoked(false)
                .build();

        return refreshTokenRepository.save(refreshToken);
    }

    public RefreshToken verifyRefreshToken(String token) {
        RefreshToken refreshToken = refreshTokenRepository.findByToken(token)
                .orElseThrow(() -> new InvalidTokenException("Refresh token not found!"));

        if (refreshToken.isRevoked()) {
            throw new InvalidTokenException("Refresh token already used!");
        }

        if (refreshToken.getExpiresAt().isBefore(LocalDateTime.now())) {
            refreshTokenRepository.delete(refreshToken);
            throw new InvalidTokenException("The refresh token has expired!");
        }

        return refreshToken;
    }

    public void revokeRefreshToken(String token) {
        RefreshToken refreshToken = refreshTokenRepository.findByToken(token)
                .orElseThrow(() -> new InvalidTokenException("Refresh token not found!"));

        refreshToken.setRevoked(true);
        refreshTokenRepository.save(refreshToken);
    }
}
