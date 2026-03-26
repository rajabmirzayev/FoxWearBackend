package com.foxwear.authservice.dto.response;

import com.foxwear.common.enums.Gender;
import com.foxwear.common.enums.UserStatus;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserUpdateResponse {

    Long id;
    String firstName;
    String lastName;
    String username;
    String email;
    String phoneNumber;
    Gender gender;
    LocalDate birthDate;
    String profilePicture;
    UserStatus status;

}
