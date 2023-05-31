package com.example.jwt.global.config;

import com.example.jwt.global.common.RedisDao;
import com.example.jwt.global.jwt.JwtAccessDeniedHandler;
import com.example.jwt.global.jwt.JwtAuthenticationEntryPoint;
import com.example.jwt.global.jwt.TokenProvider;
import com.example.jwt.global.jwt.util.JwtSecurityConfig;
import com.example.jwt.global.security.CustomAuthenticationProvider;
import com.example.jwt.global.security.CustomUserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationProvider;
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
    private final String AUTH_WHITELIST = "/**/public/**";
    private final CustomUserDetailsService customUserDetailsService;
    private final RedisDao redisDao;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        return new CustomAuthenticationProvider(customUserDetailsService, passwordEncoder());
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
                .apply(new JwtSecurityConfig(tokenProvider, redisDao));

    }
}