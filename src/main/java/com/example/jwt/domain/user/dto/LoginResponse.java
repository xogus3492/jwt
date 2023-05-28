package com.example.jwt.domain.user.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class LoginResponse {

    private String token;

    public static LoginResponse of(String token) {
        return LoginResponse.builder()
                .token(token)
                .build();
    }
}
