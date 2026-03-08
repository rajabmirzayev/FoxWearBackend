package com.rgb.foxwear.dto.request.auth;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.rgb.foxwear.enums.Gender;
import jakarta.validation.constraints.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RegisterRequest {
    @NotBlank(message = "First name is required")
    @Size(min = 3, max = 30, message = "First name must be between 3 and 30 characters")
    String firstName;

    @NotBlank(message = "Last name is required")
    @Size(min = 3, max = 50, message = "Last name must be between 3 and 50 characters")
    String lastName;

    @NotBlank(message = "Username is required")
    @Size(min = 3, max = 50, message = "Username must be between 3 and 50 characters")
    String username;

    @Email(message = "Please provide a valid email address")
    @NotBlank(message = "Email is required")
    String email;

    @NotBlank(message = "Phone number is required")
    @Pattern(regexp = "^\\+994\\s(50|51|55|70|77|99|10)\\s\\d{3}\\s\\d{2}\\s\\d{2}$", message = "Phone number must this format: +994 12 345 67 89)")
    String phoneNumber;

    @NotBlank(message = "Password is required")
    @Size(min = 8, max = 255, message = "Password must be at least 8 characters long")
    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!]).*$",
            message = "Password must contain at least one digit, one lowercase, one uppercase letter, and one special character")
    String password;

    @NotBlank(message = "Confirm password is required")
    String confirmPassword;

    @NotNull(message = "Gender must be specified")
    Gender gender;

    @Past(message = "Birth date must be in the past")
    @JsonFormat(pattern = "dd-MM-yyyy")
    LocalDate birthDate;
}
