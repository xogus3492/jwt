package com.example.jwt.global.config;

import com.example.jwt.global.jwt.JwtAccessDeniedHandler;
import com.example.jwt.global.jwt.JwtAuthenticationEntryPoint;
import com.example.jwt.global.jwt.TokenProvider;
import com.example.jwt.global.jwt.util.JwtSecurityConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    private final TokenProvider tokenProvider;
    private final String[] AUTH_WHITELIST = {
            "/auth/signup",
            "/auth/signin"
    }; // 권한이 필요 없는 경로들

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.
                sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)

                .and()
                .httpBasic().disable()
                .formLogin().disable()

                .cors()
                .and()
                .csrf().disable()

                .authorizeRequests()
                .antMatchers(AUTH_WHITELIST).permitAll()
                //.antMatchers().authenticated() // 권한 o
                .anyRequest().authenticated()

                .and()
                .exceptionHandling()
                .accessDeniedHandler(new JwtAccessDeniedHandler())
                .authenticationEntryPoint(new JwtAuthenticationEntryPoint())

                .and()
                .apply(new JwtSecurityConfig(tokenProvider));

    }
}