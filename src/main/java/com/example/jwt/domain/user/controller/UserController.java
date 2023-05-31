package com.example.jwt.domain.user.controller;

import com.example.jwt.domain.user.dto.CommonUserResponse;
import com.example.jwt.domain.user.dto.SignupRequest;
import com.example.jwt.domain.user.dto.SignupResponse;
import com.example.jwt.domain.user.service.UserService;
import com.example.jwt.global.security.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/public/signup")
    public ResponseEntity<SignupResponse> signup(
            @RequestBody @Valid SignupRequest request
    ) {
        SignupResponse response = userService.signup(request);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/user")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity<CommonUserResponse> readMyUserInfo(@AuthenticationPrincipal CustomUserDetails customUserDetails) {

        return ResponseEntity.ok(userService.readMyUserInfo(customUserDetails.getId()));
    }

    @GetMapping("/user/{username}")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<CommonUserResponse> searchUserInfo(@PathVariable String username) {
        return ResponseEntity.ok(userService.searchUserInfo(username));
    }
}
