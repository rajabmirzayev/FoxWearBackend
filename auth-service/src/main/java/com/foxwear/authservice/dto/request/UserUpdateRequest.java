package com.foxwear.authservice.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.foxwear.common.enums.Gender;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserUpdateRequest {

    @NotBlank
    @Size(max = 30)
    String firstName;

    @NotBlank
    @Size(max = 50)
    String lastName;

    @NotBlank
    @Size(min = 3, max = 255)
    String username;

    @Email
    @NotBlank
    String email;

    @Pattern(
            regexp = "^(\\+994\\s(50|51|55|70|77|99|10)\\s\\d{3}\\s\\d{2}\\s\\d{2})?$",
            message = "Phone number must this format: +994 12 345 67 89)"
    )
    String phoneNumber;

    @NotNull
    @Enumerated(EnumType.STRING)
    Gender gender;

    @Column(name = "birth_date")
    @JsonFormat(pattern = "dd-MM-yyyy")
    LocalDate birthDate;

    @Size(max = 10000)
    String profilePicture;

}
