package com.example.jwt.domain.user.service;

import com.example.jwt.domain.user.domain.User;
import com.example.jwt.domain.user.domain.repository.UserRepository;
import com.example.jwt.domain.user.dto.SignupRequest;
import com.example.jwt.domain.user.dto.SignupResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public SignupResponse signup(SignupRequest request) {
        User user = userRepository.save(request.toEntity(passwordEncoder.encode(request.getPassword())));

        return new SignupResponse(user.getId());
    }
}
