package com.rgb.foxwear.dto.response.auth;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserGetReviewResponse {

    String firstName;
    String lastName;
    String profilePicture;

}
