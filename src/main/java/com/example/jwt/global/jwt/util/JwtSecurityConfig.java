package com.example.jwt.global.jwt.util;

import com.example.jwt.global.common.RedisDao;
import com.example.jwt.global.jwt.JwtFilter;
import com.example.jwt.global.jwt.TokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@RequiredArgsConstructor
public class JwtSecurityConfig extends SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity> {
    private final TokenProvider jwtTokenProvider;
    private final RedisDao redisDao;

    @Override
    public void configure(HttpSecurity http) {

        http.addFilterBefore(new JwtFilter(jwtTokenProvider, redisDao), UsernamePasswordAuthenticationFilter.class);

    }
}
