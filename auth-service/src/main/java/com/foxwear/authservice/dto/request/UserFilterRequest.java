package com.foxwear.authservice.dto.request;

import com.foxwear.common.enums.Gender;
import com.foxwear.common.enums.Role;
import com.foxwear.common.enums.UserStatus;
import jakarta.validation.constraints.Pattern;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Sort;

import java.util.List;

@Getter
@Setter
@ToString
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserFilterRequest {

    Integer page;
    Integer size;
    List<Gender> genders;
    List<Role> roles;
    List<UserStatus> statuses;
    Boolean isEmailVerified;
    Boolean isPhoneNumberVerified;
    Boolean twoFactorEnabled;
    Sort.Direction direction = Sort.Direction.DESC;
    String searchKeyword;

    @Pattern(regexp = "updatedAt|createdAt|firstName|lastName|username|birthDate", message = "Invalid sort field")
    String sortBy = "updatedAt";

}
