package com.foxwear.interactionservice.controller;

import com.foxwear.common.dto.ApiResponse;
import com.foxwear.interactionservice.dto.request.MessageCreateRequest;
import com.foxwear.interactionservice.dto.response.MessageCreateResponse;
import com.foxwear.interactionservice.service.MessageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/messages/contact")
@RequiredArgsConstructor
@Tag(name = "Message Controller", description = "API for managing contact messages")
public class MessageController {
    private final MessageService messageService;

    @Operation(summary = "Create a new contact message", description = "Allows a user to send a message via the contact form")
    @PostMapping
    public ResponseEntity<ApiResponse<MessageCreateResponse>> createMessage(
            @RequestBody MessageCreateRequest messageCreateRequest,
            @RequestHeader(name = "X-User-Id") Long userId
    ) {
        var response = messageService.createMessage(messageCreateRequest, userId);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

}
