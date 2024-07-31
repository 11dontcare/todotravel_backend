package org.example.todotravel.domain.user.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.todotravel.domain.user.dto.request.UserRegisterRequestDto;
import org.example.todotravel.domain.user.entity.Role;
import org.example.todotravel.domain.user.entity.User;
import org.example.todotravel.domain.user.repository.UserRepository;
import org.example.todotravel.domain.user.service.UserService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Override
    @Transactional(readOnly = true)
    public Optional<User> getUserByUserId(Long userId) {
        return userRepository.findById(userId);
    }

    // 일반 회원가입 유저 등록
    @Override
    @Transactional
    public User registerNewUser(UserRegisterRequestDto dto, PasswordEncoder passwordEncoder) {
        User newUser = User.builder()
            .username(dto.getUsername())
            .password(passwordEncoder.encode(dto.getPassword()))
            .nickname(dto.getNickname())
            .name(dto.getName())
            .gender(dto.getGender())
            .email(dto.getEmail())
            .createdDate(LocalDateTime.now())
            .birthDate(dto.getBirthDate())
            .role(Role.ROLE_USER)
            .build();

        return userRepository.save(newUser);
    }
}
