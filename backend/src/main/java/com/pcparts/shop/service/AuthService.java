package com.pcparts.shop.service;

import com.pcparts.shop.dto.auth.LoginRequest;
import com.pcparts.shop.dto.auth.LoginResponse;
import com.pcparts.shop.dto.auth.RegisterRequest;
import com.pcparts.shop.entity.Role;
import com.pcparts.shop.entity.User;
import com.pcparts.shop.exception.BadRequestException;
import com.pcparts.shop.repository.RoleRepository;
import com.pcparts.shop.repository.UserRepository;
import com.pcparts.shop.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider tokenProvider;

    public void register(RegisterRequest request) {
        String username = request.username().trim();
        String email = request.email().trim();
        // Enforce unique identity constraints before attempting to persist a user
        if (userRepository.existsByUsernameIgnoreCase(username)) {
            throw new BadRequestException("Username is already taken");
        }
        if (userRepository.existsByEmailIgnoreCase(email)) {
            throw new BadRequestException("Email is already in use");
        }

        // Attach the default ROLE_USER so new accounts can access protected resources immediately
        Role roleUser = roleRepository.findByName(Role.RoleName.ROLE_USER)
                .orElseThrow(() -> new BadRequestException("Default role ROLE_USER not configured"));

        User user = User.builder()
                .username(username)
                .email(email)
                .password(passwordEncoder.encode(request.password()))
                .fullName(request.fullName())
                .gender(request.gender())
                .dateOfBirth(request.dateOfBirth())
                .roles(Set.of(roleUser))
                .build();
        userRepository.save(user);
    }

    public LoginResponse login(LoginRequest request) {
        String identifier = request.usernameOrEmail().trim();
        // Delegate credential validation to Spring Security, which raises on failure
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(identifier, request.password()));

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        // Produce a signed JWT and expose the caller's roles for client-side routing decisions
        String token = tokenProvider.generateToken(userDetails);
        Set<String> roles = userDetails.getAuthorities().stream()
                .map(authority -> authority.getAuthority())
                .collect(Collectors.toSet());
        return new LoginResponse("Bearer " + token, userDetails.getUsername(), roles);
    }
}


