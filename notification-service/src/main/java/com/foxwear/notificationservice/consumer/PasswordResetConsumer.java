package com.foxwear.notificationservice.consumer;

import com.foxwear.common.event.PasswordResetEvent;
import com.foxwear.notificationservice.service.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PasswordResetConsumer {
    private final EmailService emailService;

    @KafkaListener(topics = "password-reset-topic", groupId = "notification-group")
    public void handlePasswordReset(PasswordResetEvent event) {
        emailService.sendPasswordResetEmail(event);
    }

}
