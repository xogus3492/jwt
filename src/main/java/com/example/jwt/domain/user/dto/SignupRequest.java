package com.example.jwt.domain.user.dto;

import com.example.jwt.domain.user.domain.RoleType;
import com.example.jwt.domain.user.domain.User;
import lombok.Getter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
public class SignupRequest {

    @Email
    @NotBlank
    private String email;

    @NotBlank
    private String password;

    @NotBlank
    private String name;

    @NotNull
    private RoleType roleType;

    public User toEntity(String pw) {
        return User.builder()
                .email(email)
                .password(pw)
                .name(name)
                .roleType(roleType)
                .build();
    }
}
