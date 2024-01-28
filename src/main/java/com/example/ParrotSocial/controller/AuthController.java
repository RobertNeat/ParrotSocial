package com.example.ParrotSocial.controller;


import com.example.ParrotSocial.request.AuthenticationRequest;
import com.example.ParrotSocial.request.RegisterRequest;
import com.example.ParrotSocial.response.AuthenticationReposnse;
import com.example.ParrotSocial.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin("http://127.0.0.1:5500")
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService service;


    @PostMapping("/register")
    public ResponseEntity<AuthenticationReposnse> register(@RequestBody RegisterRequest request){
        return ResponseEntity.ok(service.register(request));
    }
    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationReposnse> authenticate(@RequestBody AuthenticationRequest request){
        return ResponseEntity.ok(service.authenticate(request));
    }

}
