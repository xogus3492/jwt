package com.example.jwt.domain.user.service;

import com.example.jwt.domain.user.domain.User;
import com.example.jwt.domain.user.domain.repository.UserRepository;
import com.example.jwt.domain.user.dto.CommonUserResponse;
import com.example.jwt.domain.user.dto.SignupRequest;
import com.example.jwt.domain.user.dto.SignupResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;

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

    public CommonUserResponse readMyUserInfo(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User Not Found"));

        return CommonUserResponse.of(user);
    }

    public CommonUserResponse searchUserInfo(String name) {
        User user = userRepository.findByName(name)
                .orElseThrow(() -> new EntityNotFoundException("User Not Found"));

        return CommonUserResponse.of(user);
    }
}
