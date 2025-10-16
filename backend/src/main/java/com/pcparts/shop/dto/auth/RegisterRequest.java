package com.pcparts.shop.dto.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public record RegisterRequest(
        @NotBlank(message = "Username is required")
        @Size(min = 3, max = 50, message = "Username must be between 3 and 50 characters")
        String username,

        @NotBlank(message = "Email is required")
        @Email(message = "Email must be valid")
        String email,

        @NotBlank(message = "Password is required")
        @Size(min = 8, max = 100, message = "Password must be between 8 and 100 characters")
        @Pattern(regexp = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[!@#$%^&*]).+$",
                message = "Password must contain upper, lower, digit, and special character")
        String password,

        @NotBlank(message = "Full name is required")
        String fullName,

        String gender,

        @NotNull(message = "Date of birth is required")
        LocalDate dateOfBirth
) {
}


