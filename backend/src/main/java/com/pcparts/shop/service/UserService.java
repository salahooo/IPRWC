package com.pcparts.shop.service;

import com.pcparts.shop.dto.user.ChangePasswordRequest;
import com.pcparts.shop.dto.user.AdminResetPasswordRequest;
import com.pcparts.shop.dto.user.UpdateUserRequest;
import com.pcparts.shop.dto.user.UserResponse;
import com.pcparts.shop.entity.User;
import com.pcparts.shop.exception.BadRequestException;
import com.pcparts.shop.exception.ResourceNotFoundException;
import com.pcparts.shop.mapper.UserMapper;
import com.pcparts.shop.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserResponse getCurrentUserProfile() {
        User user = getCurrentUser();
        return UserMapper.toDto(user);
    }

    public UserResponse updateCurrentUser(UpdateUserRequest request) {
        User user = getCurrentUser();
        // Prevent users from hijacking an email that already belongs to another account
        if (!user.getEmail().equalsIgnoreCase(request.email()) && userRepository.existsByEmail(request.email())) {
            throw new BadRequestException("Email is already in use");
        }
        UserMapper.updateEntity(user, request);
        userRepository.save(user);
        return UserMapper.toDto(user);
    }

    public void changePassword(ChangePasswordRequest request) {
        User user = getCurrentUser();
        // Require the current password to harden against CSRF or stolen sessions
        if (!passwordEncoder.matches(request.oldPassword(), user.getPassword())) {
            throw new BadRequestException("Old password is incorrect");
        }
        user.setPassword(passwordEncoder.encode(request.newPassword()));
        userRepository.save(user);
    }

    public List<UserResponse> findAllUsers() {
        return userRepository.findAll().stream()
                .map(UserMapper::toDto)
                .collect(Collectors.toList());
    }

    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new ResourceNotFoundException("User not found with id: " + id);
        }
        userRepository.deleteById(id);
    }

    public void adminResetPassword(Long id, AdminResetPasswordRequest request) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
        user.setPassword(passwordEncoder.encode(request.newPassword()));
        userRepository.save(user);
    }

    private User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        // Resolve the username from the security context into the persistent user record
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + username));
    }
}


