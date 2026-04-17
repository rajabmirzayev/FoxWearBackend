package com.foxwear.interactionservice.service;

import com.foxwear.common.exception.UnauthorizedException;
import com.foxwear.interactionservice.dto.request.MessageCreateRequest;
import com.foxwear.interactionservice.dto.response.MessageCreateResponse;
import com.foxwear.interactionservice.entity.Message;
import com.foxwear.interactionservice.mapper.MessageMapper;
import com.foxwear.interactionservice.repository.MessageRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class MessageService {
    private final MessageMapper messageMapper;
    private final MessageRepository messageRepository;

    /**
     * Creates a new message based on the provided request and user ID.
     *
     * @param request the message creation details
     * @param userId  the ID of the user creating the message
     * @return the response containing created message details
     * @throws UnauthorizedException if the userId is null
     */
    public MessageCreateResponse createMessage(
            MessageCreateRequest request,
            Long userId
    ) {
        log.info("Creating message for user: {} with request: {}", userId, request);
        checkUserIdIsNull(userId);

        Message message = messageMapper.toEntity(request);
        var savedMessage = messageRepository.save(message);
        log.info("Message successfully saved with ID: {}", savedMessage.getId());

        return messageMapper.toCreateResponse(savedMessage);
    }

    private void checkUserIdIsNull(Long userId) {
        if (userId == null) {
            throw new UnauthorizedException("Unauthorized user");
        }
    }
}
