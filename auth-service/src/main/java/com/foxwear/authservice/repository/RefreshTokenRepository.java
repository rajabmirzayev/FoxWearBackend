package com.foxwear.authservice.repository;

import com.foxwear.authservice.entity.RefreshToken;
import com.foxwear.authservice.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {

    void deleteByUser(UserEntity user);

    Optional<RefreshToken> findByToken(String token);

}
