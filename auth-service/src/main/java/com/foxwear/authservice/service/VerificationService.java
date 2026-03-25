package com.foxwear.authservice.service;

import com.foxwear.common.event.RegistrationEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class VerificationService {
    private final RedisTemplate<String, Object> redisTemplate;
    private final KafkaTemplate<String, Object> kafkaTemplate;

    public void createAndSendVerification(String email) {
        String token = UUID.randomUUID().toString();

        redisTemplate.opsForValue().set("CONFIRM:" + token, email, Duration.ofMinutes(15));

        RegistrationEvent emailData = RegistrationEvent.builder()
                .email(email)
                .token(token)
                .link("http://localhost:8080/api/v1/auth/confirm?token=" + token)
                .build();

        kafkaTemplate.send("registration-topic", emailData);
    }
}
