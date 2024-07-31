package org.example.todotravel.domain.user.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.todotravel.domain.user.dto.request.UserRegisterRequestDto;
import org.example.todotravel.domain.user.entity.User;
import org.example.todotravel.domain.user.service.impl.UserServiceImpl;
import org.example.todotravel.global.controller.ApiResponse;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class UserController {
    private final UserServiceImpl userService;
    private final PasswordEncoder passwordEncoder;

    @PostMapping("/signup")
    public ApiResponse<?> registerUser(@Valid @RequestBody UserRegisterRequestDto dto) {
        User newUser = userService.registerNewUser(dto, passwordEncoder);
        return new ApiResponse<>(true, "회원가입 성공", newUser);
    }
}
