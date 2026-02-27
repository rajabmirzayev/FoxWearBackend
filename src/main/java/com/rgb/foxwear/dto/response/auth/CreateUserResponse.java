package com.rgb.foxwear.dto.response.auth;

import com.rgb.foxwear.enums.Gender;
import com.rgb.foxwear.enums.Role;
import com.rgb.foxwear.enums.UserStatus;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CreateUserResponse {

    Long id;
    String firstName;
    String lastName;
    String username;
    String email;
    String phoneNumber;
    Gender gender;
    Role role;
    UserStatus status;
    LocalDateTime createdAt;

}