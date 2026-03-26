package com.foxwear.interactionservice.service;

import org.springframework.kafka.core.KafkaTemplate;
import com.foxwear.common.event.LikeEvent;
import com.foxwear.common.exception.UnauthorizedException;
import com.foxwear.interactionservice.entity.ProductLike;
import com.foxwear.interactionservice.repository.ProductLikeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Slf4j
public class LikeService {
    private final ProductLikeRepository productLikeRepository;
    private final KafkaTemplate<String, Object> kafkaTemplate;

    /**
     * Toggles a like status for a product by a specific user.
     * If the like exists, it is removed (unlike). If it doesn't, it is created.
     *
     * @param productId The ID of the product to like/unlike
     * @param userId    The ID of the user performing the action
     * @return true if the product is now liked, false if unliked
     */
    @Transactional
    public boolean likeProduct(Long productId, Long userId) {
        log.info("Processing like for product ID: {} by user ID: {}", productId, userId);
        boolean isLiked;

        if (userId == null) {
            log.error("Like operation failed: User ID is null");
            throw new UnauthorizedException("Unauthorized access");
        }

        if (productLikeRepository.existsByProductIdAndUserId(productId, userId)) {
            log.info("User {} unliked product {}.", userId, productId);
            productLikeRepository.deleteByProductIdAndUserId(productId, userId);
            isLiked = false;
        } else {
            ProductLike productLike = ProductLike.builder()
                    .productId(productId)
                    .userId(userId)
                    .likedAt(LocalDateTime.now())
                    .build();

            productLikeRepository.save(productLike);
            log.info("User {} liked product {}.", userId, productId);
            isLiked = true;
        }

        // Prepare event for Kafka to synchronize with other services (e.g., product-service for counters)
        LikeEvent likeEvent = LikeEvent.builder()
                .productId(productId)
                .isLiked(isLiked)
                .build();

        log.debug("Sending LikeEvent to Kafka topic 'product-like-topic' for product ID: {}", productId);
        kafkaTemplate.send("product-like-topic", productId.toString(), likeEvent);

        return isLiked;
    }

    /**
     * Retrieves all product IDs that a specific user has liked.
     *
     * @param userId The ID of the user
     * @return A set of product IDs
     */
    @Transactional(readOnly = true)
    public Set<Long> getMyLikedIds(Long userId) {
        log.info("Fetching liked product IDs for user ID: {}", userId);

        if (userId == null) {
            return Collections.emptySet();
        }

        return productLikeRepository.findAllProductIdsByUserId(userId);
    }
}
