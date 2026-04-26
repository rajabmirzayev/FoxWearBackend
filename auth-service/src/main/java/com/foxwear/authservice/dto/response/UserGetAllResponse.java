package com.foxwear.authservice.dto.response;

import com.foxwear.common.enums.Gender;
import com.foxwear.common.enums.Role;
import com.foxwear.common.enums.UserStatus;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserGetAllResponse {

    Long id;
    String firstName;
    String lastName;
    String username;
    String email;
    String phoneNumber;
    String profilePicture;
    Gender gender;
    Role role;
    UserStatus status;
    Boolean isEmailVerified;
    Boolean isPhoneNumberVerified;
    Boolean twoFactorEnabled;

}
