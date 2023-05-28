package com.example.jwt.domain.user.dto;

import com.example.jwt.domain.user.domain.RoleType;
import com.example.jwt.domain.user.domain.User;
import lombok.Getter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Getter
public class SignupRequest {

    @Email
    @NotBlank
    private String email;

    @NotBlank
    private String password;

    @NotBlank
    private String name;

    public User toEntity(String pw) {
        return User.builder()
                .email(email)
                .password(pw)
                .name(name)
                .roleType(RoleType.USER)
                .build();
    }
}
