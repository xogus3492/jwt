package com.example.jwt.domain.user.service;

import com.example.jwt.domain.user.dto.*;
import com.example.jwt.global.common.RedisDao;
import com.example.jwt.global.jwt.TokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.util.Objects;


@Service
@Transactional
@RequiredArgsConstructor
public class AuthService {
    private final TokenProvider tokenProvider;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final RedisDao redisDao;

    public LoginResponse login(LoginRequest request) {
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword());

        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        // autenticationManagerBuilder.getObject() => AuthenticationManager 리턴
        // AuthenticationManager의 authenticate()에 authenticationToken을 넣어 호출 하게 되면,
        // AuthenticationManager는 등록된 AuthenticationProvider 목록을 순차적으로 확인하며,
        // 적합한 AuthenticationProvider를 찾아 AuthenticationProvider의 authenticate()를 호출
        // 인증 결과에 따라 Authentication 객체 리턴
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String atk = tokenProvider.provideAccessToken(authentication);
        String rtk = tokenProvider.provideRefreshToken(authentication);

        return LoginResponse.of(atk, rtk);
    }

    public ReissueResponse reissue(ReissueRequest request) {
        String email = tokenProvider.getAuthentication(request.getRefreshToken()).getName();

        String rtk = redisDao.getValues(email);

        if (!tokenProvider.validateToken(rtk)) {
            throw new RuntimeException("잘못된 JWT 서명입니다.");
        }

        Authentication authentication = tokenProvider.getAuthentication(rtk);
        String atk = tokenProvider.provideAccessToken(authentication);

        return ReissueResponse.of(atk);
    }

    public void logout(LogoutRequest request) {
        String atk = request.getAccessToken();

        if (!tokenProvider.validateToken(atk)) {
            throw new IllegalArgumentException("잘못된 JWT 서명입니다.");
        }

        Authentication authentication = tokenProvider.getAuthentication(atk);

        if (redisDao.getValues(authentication.getName()) != null){
            redisDao.deleteValues(authentication.getName());
        }

        Long expiration = tokenProvider.getExpiration(atk);
        redisDao.setValues(atk,"logout", Duration.ofMillis(expiration));
    }
}
