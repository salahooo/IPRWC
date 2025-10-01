package com.fatbike.shop.service;

import com.fatbike.shop.dto.user.ChangePasswordRequest;
import com.fatbike.shop.dto.user.UpdateUserRequest;
import com.fatbike.shop.dto.user.UserResponse;
import com.fatbike.shop.entity.User;
import com.fatbike.shop.exception.BadRequestException;
import com.fatbike.shop.exception.ResourceNotFoundException;
import com.fatbike.shop.mapper.UserMapper;
import com.fatbike.shop.repository.UserRepository;
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
        if (!user.getEmail().equalsIgnoreCase(request.email()) && userRepository.existsByEmail(request.email())) {
            throw new BadRequestException("Email is already in use");
        }
        UserMapper.updateEntity(user, request);
        userRepository.save(user);
        return UserMapper.toDto(user);
    }

    public void changePassword(ChangePasswordRequest request) {
        User user = getCurrentUser();
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

    private User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + username));
    }
}
