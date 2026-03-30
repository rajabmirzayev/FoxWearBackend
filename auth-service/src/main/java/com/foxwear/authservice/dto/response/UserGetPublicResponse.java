package com.foxwear.authservice.dto.response;

import com.foxwear.common.enums.Role;
import com.foxwear.common.enums.UserStatus;
import lombok.*;
import lombok.experimental.FieldDefaults;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserGetPublicResponse {

    Long id;
    String firstName;
    String lastName;
    String username;
    String profilePicture;
    Role role;

}
