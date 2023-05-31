package com.example.jwt.domain.user.controller;

import com.example.jwt.domain.user.dto.LoginRequest;
import com.example.jwt.domain.user.dto.LoginResponse;
import com.example.jwt.domain.user.dto.ReissueResponse;
import com.example.jwt.domain.user.service.AuthService;
import com.example.jwt.global.security.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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

    @PostMapping("/public/reissue")
    public ResponseEntity<ReissueResponse> reissue(
            @AuthenticationPrincipal CustomUserDetails customUserDetails
    ) {
        ReissueResponse response = authService.reissue(customUserDetails.getUsername());

        return ResponseEntity.ok().body(response);
    }
}
