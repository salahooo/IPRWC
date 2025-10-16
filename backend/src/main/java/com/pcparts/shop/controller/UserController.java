package com.pcparts.shop.controller;

import com.pcparts.shop.dto.user.ChangePasswordRequest;
import com.pcparts.shop.dto.user.AdminResetPasswordRequest;
import com.pcparts.shop.dto.user.UpdateUserRequest;
import com.pcparts.shop.dto.user.UserResponse;
import com.pcparts.shop.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/me")
    public ResponseEntity<UserResponse> getProfile() {
        // Signed-in users can fetch their own profile details
        return ResponseEntity.ok(userService.getCurrentUserProfile());
    }

    @PutMapping("/update")
    public ResponseEntity<UserResponse> updateProfile(@Valid @RequestBody UpdateUserRequest request) {
        // Persist profile edits after validation (email uniqueness enforced in the service)
        return ResponseEntity.ok(userService.updateCurrentUser(request));
    }

    @PostMapping("/change-password")
    public ResponseEntity<Void> changePassword(@Valid @RequestBody ChangePasswordRequest request) {
        // Requires current password and returns 204 when the change sticks
        userService.changePassword(request);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<UserResponse>> findAll() {
        // Admin-only directory of every registered account
        return ResponseEntity.ok(userService.findAllUsers());
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        // Admin cleanup of inactive/problematic accounts
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/reset-password")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> adminResetPassword(@PathVariable Long id, @Valid @RequestBody AdminResetPasswordRequest request) {
        // Admin ability to reset a user's password without the old password
        userService.adminResetPassword(id, request);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}


