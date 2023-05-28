package com.example.jwt.domain.user.domain;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum RoleType {

    ADMIN, USER;

    @JsonCreator
    public static RoleType fromString(String value) {
        return RoleType.valueOf(value.toUpperCase());
    }
}
