package com.foxwear.interactionservice.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class MessageCreateRequest {

    @NotNull(message = "User ID is required")
    Long userId;

    @NotBlank(message = "Email is required")
    String email;

    @NotBlank(message = "Full name is required")
    String fullName;

    @NotBlank(message = "Subject is required")
    String subject;

    @NotBlank(message = "Message content is required")
    String message;

}
