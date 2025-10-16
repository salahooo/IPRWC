package com.pcparts.shop.mapper;

import com.pcparts.shop.dto.user.UpdateUserRequest;
import com.pcparts.shop.dto.user.UserResponse;
import com.pcparts.shop.entity.User;

import java.util.stream.Collectors;

public final class UserMapper {

    private UserMapper() {
    }

    public static UserResponse toDto(User user) {
        return new UserResponse(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getFullName(),
                user.getGender(),
                user.getDateOfBirth(),
                user.getRoles().stream()
                        .map(role -> role.getName().name())
                        .collect(Collectors.toSet())
        );
    }

    public static void updateEntity(User user, UpdateUserRequest request) {
        user.setFullName(request.fullName());
        user.setGender(request.gender());
        user.setDateOfBirth(request.dateOfBirth());
        user.setEmail(request.email());
    }
}


