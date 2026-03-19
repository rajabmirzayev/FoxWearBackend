package com.rgb.foxwear.dto.response.user;

import com.rgb.foxwear.enums.Gender;
import com.rgb.foxwear.enums.Role;
import com.rgb.foxwear.enums.UserStatus;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserGetResponse {

    Long id;
    String firstName;
    String lastName;
    String username;
    String email;
    String phoneNumber;
    Gender gender;
    LocalDate birthDate;
    String profilePicture;
    Role role;
    UserStatus status;
    boolean isEmailVerified = false;
    boolean isPhoneNumberVerified = false;
    boolean twoFactorEnabled = false;

}
