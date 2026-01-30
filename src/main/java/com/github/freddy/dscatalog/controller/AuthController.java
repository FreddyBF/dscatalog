package com.github.freddy.dscatalog.controller;


import com.github.freddy.dscatalog.dto.user.LoginRequest;
import com.github.freddy.dscatalog.dto.user.LoginResponse;
import com.github.freddy.dscatalog.dto.user.UserRequest;
import com.github.freddy.dscatalog.dto.user.UserResponse;
import com.github.freddy.dscatalog.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody @Valid LoginRequest dto) {
        return  ResponseEntity.ok(authService.login(dto));
    }

    @PostMapping("/register")
    public ResponseEntity<UserResponse> register(@RequestBody @Valid UserRequest dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(authService.register(dto));
    }

    @PostMapping("/refresh")
    public ResponseEntity<?> refresh(@RequestBody @Valid  LoginRequest dto) {
        return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).build();
    }


}
