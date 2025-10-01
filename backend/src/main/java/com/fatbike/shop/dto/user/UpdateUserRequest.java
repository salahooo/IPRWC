package com.fatbike.shop.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record UpdateUserRequest(
        @NotBlank(message = "Full name is required")
        String fullName,
        String gender,
        @NotNull(message = "Date of birth is required")
        LocalDate dateOfBirth,
        @NotBlank(message = "Email is required")
        @Email(message = "Email must be valid")
        String email
) {
}
