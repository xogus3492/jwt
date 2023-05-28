package com.example.jwt.domain.user.domain;

import com.example.jwt.global.common.BaseTimeEntity;
import lombok.Builder;
import lombok.Getter;

import javax.persistence.*;

@Entity
@Getter
public class User extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @Column(length = 40, unique = true, nullable = false, updatable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(length = 10, unique = true, nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RoleType roleType;

    @Builder
    public User(Long id, String email, String password, String name, RoleType roleType) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.name = name;
        this.roleType = roleType;
    }
}
