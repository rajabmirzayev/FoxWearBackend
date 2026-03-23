package com.foxwear.productservice.kafka.consumer;

import com.foxwear.common.event.LikeEvent;
import com.foxwear.productservice.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class LikeKafkaConsumer {
    private final ProductRepository productRepository;

    @KafkaListener(topics = "product-like-topic", groupId = "product-group")
    public void consume(LikeEvent event) {
        log.info("Kafka Received: Product {} isLiked: {}", event.getProductId(), event.isLiked());

        productRepository.findById(event.getProductId()).ifPresent(product -> {
            if (event.isLiked()) {
                product.setLikedCount(product.getLikedCount() + 1);
            } else {
                product.setLikedCount(Math.max(0, product.getLikedCount() - 1));
            }
            productRepository.save(product);
        });
    }
}
