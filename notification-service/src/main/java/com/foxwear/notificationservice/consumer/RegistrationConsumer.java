package com.foxwear.notificationservice.consumer;

import com.foxwear.common.event.RegistrationEvent;
import com.foxwear.notificationservice.service.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RegistrationConsumer {
    private final EmailService emailService;

    @KafkaListener(topics = "registration-topic", groupId = "notification-group")
    public void handleRegistration(RegistrationEvent event) {
        emailService.sendRegistrationEmail(event);
    }

    @KafkaListener(topics = "verify-email-success", groupId = "notification-group")
    public void handleVerifyEmailSuccess(String email) {
        emailService.sendAccountActivatedAlert(email);
    }
}