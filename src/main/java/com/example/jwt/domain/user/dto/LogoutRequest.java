package com.example.jwt.domain.user.dto;

import lombok.Getter;

import javax.validation.constraints.NotBlank;

@Getter
public class LogoutRequest {

    @NotBlank
    private String accessToken;
}
