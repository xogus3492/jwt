package com.example.jwt.domain.user.service;

import com.example.jwt.domain.user.domain.User;
import com.example.jwt.domain.user.domain.repository.UserRepository;
import com.example.jwt.domain.user.dto.LoginRequest;
import com.example.jwt.domain.user.dto.LoginResponse;
import com.example.jwt.global.jwt.TokenProvider;
import com.example.jwt.global.security.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class AuthService {
    private final TokenProvider tokenProvider;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final UserRepository userRepository;

    public LoginResponse login(LoginRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new EntityNotFoundException());

        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(
                        new CustomUserDetails(user.getId(), user.getEmail(), user.getPassword(), user.getRoleType())
                        , request.getPassword());


        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        // autenticationManagerBuilder.getObject() => AuthenticationManager 리턴
        // AuthenticationManager의 authenticate()에 authenticationToken을 넣어 호출 하게 되면,
        // AuthenticationManager는 등록된 AuthenticationProvider 목록을 순차적으로 확인하며,
        // 적합한 AuthenticationProvider를 찾아 AuthenticationProvider의 authenticate()를 호출
        // 인증 결과에 따라 Authentication 객체 리턴
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwt = tokenProvider.createAccessToken(authentication);

        log.error("확인2 : " + jwt);

        return LoginResponse.of(jwt);
    }
}
