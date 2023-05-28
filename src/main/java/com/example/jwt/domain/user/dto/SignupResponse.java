package com.example.jwt.domain.user.dto;

import com.example.jwt.domain.user.domain.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class SignupResponse {

    private Long id;

    public static SignupResponse of(User user) {
        return SignupResponse.builder()
                .id(user.getId())
                .build();
    }
}
