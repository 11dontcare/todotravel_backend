package org.example.todotravel.domain.user.service;

import org.example.todotravel.domain.user.dto.request.UserRegisterRequestDto;
import org.example.todotravel.domain.user.entity.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public interface UserService {
    Optional<User> getUserByUserId(Long userId);

    User registerNewUser(UserRegisterRequestDto dto, PasswordEncoder passwordEncoder);
}
