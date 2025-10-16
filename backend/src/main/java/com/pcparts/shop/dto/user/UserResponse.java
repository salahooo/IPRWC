package com.pcparts.shop.dto.user;

import java.time.LocalDate;
import java.util.Set;

public record UserResponse(
        Long id,
        String username,
        String email,
        String fullName,
        String gender,
        LocalDate dateOfBirth,
        Set<String> roles
) {
}


