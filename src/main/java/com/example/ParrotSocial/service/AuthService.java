package com.example.ParrotSocial.service;


import com.example.ParrotSocial.config.JwtService;
import com.example.ParrotSocial.model.Role;
import com.example.ParrotSocial.model.User;
import com.example.ParrotSocial.repository.UserRepository;
import com.example.ParrotSocial.request.AuthenticationRequest;
import com.example.ParrotSocial.request.RegisterRequest;
import com.example.ParrotSocial.response.AuthenticationReposnse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthenticationReposnse register(RegisterRequest request) {
        var user = User.builder()
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.USER)
                .build();
        repository.save(user);
        var jwtToken = jwtService.generateToken(user);
        return AuthenticationReposnse.builder()
                .token(jwtToken)
                .build();
    }

    public AuthenticationReposnse authenticate(AuthenticationRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );
        var user = repository.findByEmail(request.getEmail())
                .orElseThrow();     //! -- zadbać o obsłużenie wyjątku w razie zlej autentykacji
        var jwtToken = jwtService.generateToken(user);
        return AuthenticationReposnse.builder()
                .token(jwtToken)
                .build();
    }
}
