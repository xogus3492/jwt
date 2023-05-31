package com.example.jwt.domain.user.service;

import com.example.jwt.domain.user.dto.LoginRequest;
import com.example.jwt.domain.user.dto.LoginResponse;
import com.example.jwt.domain.user.dto.ReissueResponse;
import com.example.jwt.global.common.RedisDao;
import com.example.jwt.global.jwt.TokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    public ReissueResponse reissue(String username) {
        String rtk = redisDao.getValues(username);

        if (!tokenProvider.validateToken(rtk)) {
            throw new RuntimeException("잘못된 JWT 서명입니다.");
        }

        Authentication authentication = tokenProvider.getAuthentication(rtk);
        String atk = tokenProvider.provideAccessToken(authentication);

        return ReissueResponse.of(atk);
    }
}
