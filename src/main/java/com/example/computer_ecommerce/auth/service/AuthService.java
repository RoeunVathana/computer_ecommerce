package com.example.computer_ecommerce.auth.service;

import com.example.computer_ecommerce.auth.dto.AuthResponse;
import com.example.computer_ecommerce.auth.dto.LoginRequest;
import com.example.computer_ecommerce.security.JwtService;
import com.example.computer_ecommerce.user.repository.AppUserRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final AppUserRepository users;
    private final JwtService jwtService;

    public AuthService(
            AuthenticationManager authenticationManager,
            AppUserRepository users,
            JwtService jwtService) {
        this.authenticationManager = authenticationManager;
        this.users = users;
        this.jwtService = jwtService;
    }

    public AuthResponse login(LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.username(), request.password()));

        var user = users.findByUsername(request.username())
                .orElseThrow();

        String role = user.getRoles().stream()
                .findFirst()
                .map(Enum::name)
                .orElseThrow(() -> new IllegalStateException("User has no assigned role"));

        return new AuthResponse(
                jwtService.generateToken(user.getUsername()),
                user.getUsername(),
                role);
    }
}
