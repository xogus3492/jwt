package com.example.jwt.domain.user.dto;

import com.example.jwt.domain.user.domain.RoleType;
import com.example.jwt.domain.user.domain.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class CommonUserResponse {

    private Long id;
    private String email;
    private String password;
    private String name;
    private String role;

    public static CommonUserResponse of(User user) {
        return CommonUserResponse.builder()
                .id(user.getId())
                .email(user.getEmail())
                .password(user.getPassword())
                .name(user.getName())
                .role(user.getRoleType().toString()).build();
    }
}
