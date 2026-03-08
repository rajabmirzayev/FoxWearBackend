package com.rgb.foxwear.repository.auth;

import com.rgb.foxwear.entity.auth.RefreshToken;
import com.rgb.foxwear.entity.auth.UserEntity;
import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RefreshTokenRepository extends JpaRepository<@NonNull RefreshToken, @NonNull Long> {
    void deleteByUser(UserEntity user);

    Optional<RefreshToken> findByToken(String token);
}
