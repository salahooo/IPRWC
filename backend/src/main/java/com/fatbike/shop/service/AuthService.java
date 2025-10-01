package com.fatbike.shop.service;

import com.fatbike.shop.dto.auth.LoginRequest;
import com.fatbike.shop.dto.auth.LoginResponse;
import com.fatbike.shop.dto.auth.RegisterRequest;
import com.fatbike.shop.entity.Role;
import com.fatbike.shop.entity.User;
import com.fatbike.shop.exception.BadRequestException;
import com.fatbike.shop.repository.RoleRepository;
import com.fatbike.shop.repository.UserRepository;
import com.fatbike.shop.security.JwtTokenProvider;
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
        if (userRepository.existsByUsername(request.username())) {
            throw new BadRequestException("Username is already taken");
        }
        if (userRepository.existsByEmail(request.email())) {
            throw new BadRequestException("Email is already in use");
        }

        Role roleUser = roleRepository.findByName(Role.RoleName.ROLE_USER)
                .orElseThrow(() -> new BadRequestException("Default role ROLE_USER not configured"));

        User user = User.builder()
                .username(request.username())
                .email(request.email())
                .password(passwordEncoder.encode(request.password()))
                .fullName(request.fullName())
                .gender(request.gender())
                .dateOfBirth(request.dateOfBirth())
                .roles(Set.of(roleUser))
                .build();
        userRepository.save(user);
    }

    public LoginResponse login(LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.usernameOrEmail(), request.password()));

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String token = tokenProvider.generateToken(userDetails);
        Set<String> roles = userDetails.getAuthorities().stream()
                .map(authority -> authority.getAuthority())
                .collect(Collectors.toSet());
        return new LoginResponse("Bearer " + token, userDetails.getUsername(), roles);
    }
}
