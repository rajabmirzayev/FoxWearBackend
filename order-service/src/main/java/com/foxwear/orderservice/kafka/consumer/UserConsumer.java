package com.foxwear.orderservice.kafka.consumer;

import com.foxwear.common.event.UserCreatedEvent;
import com.foxwear.orderservice.service.CartService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class UserConsumer {
    private final CartService cartService;

    @KafkaListener(topics = "user-created-topic", groupId = "user-group")
    public void userCreated(UserCreatedEvent userCreatedEvent) {
        log.info("User created event received");
        cartService.createCart(userCreatedEvent.getUserId());
    }
}
