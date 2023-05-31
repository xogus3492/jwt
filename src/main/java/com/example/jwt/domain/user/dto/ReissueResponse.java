package com.example.jwt.domain.user.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class ReissueResponse {

    private String accessToken;

    public static ReissueResponse of(String accessToken) {
        return ReissueResponse.builder()
                .accessToken(accessToken)
                .build();
    }
}
