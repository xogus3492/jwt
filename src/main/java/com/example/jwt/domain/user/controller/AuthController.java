package com.example.jwt.domain.user.controller;

import com.example.jwt.domain.user.dto.LoginRequest;
import com.example.jwt.domain.user.dto.LoginResponse;
import com.example.jwt.domain.user.service.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Slf4j
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/public/login")
    public ResponseEntity<LoginResponse> login(
            @RequestBody @Valid LoginRequest request
    ) {
        LoginResponse response = authService.login(request);

        return ResponseEntity.ok().body(response);
    }
}
