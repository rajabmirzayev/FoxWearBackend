package com.foxwear.interactionservice.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class MessageCreateResponse {

    Long id;
    Long userId;
    String email;
    String fullName;
    String subject;
    String message;
    boolean isAnswered;

}
