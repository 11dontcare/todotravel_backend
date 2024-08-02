package org.example.todotravel.domain.user.controller;

import lombok.RequiredArgsConstructor;
import org.example.todotravel.domain.user.dto.request.EmailRequestDto;
import org.example.todotravel.domain.user.dto.response.EmailResponseDto;
import org.example.todotravel.domain.user.entity.EmailMessage;
import org.example.todotravel.domain.user.service.impl.EmailServiceImpl;
import org.example.todotravel.global.controller.ApiResponse;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/send-mail")
public class EmailController {
    private final EmailServiceImpl emailService;
    private final PasswordEncoder passwordEncoder;

    // 임시 비밀번호 발급
    @PostMapping("/password")
    public ApiResponse<?> sendPasswordMail(@RequestBody EmailRequestDto emailRequestDto) {
        EmailMessage emailMessage = EmailMessage.builder()
            .to(emailRequestDto.getEmail())
            .subject("[ToDoTravel] 임시 비밀번호 발급")
            .build();

        emailService.sendMail(emailMessage, "password", passwordEncoder);

        return new ApiResponse<>(true, "임시 비밀번호 발급 성공", null);
    }

    // 회원가입 이메일 인증 - 요청 시 body로 인증번호 반환
    @PostMapping("/email")
    public ApiResponse<?> sendJoinMail(@RequestBody EmailRequestDto emailRequestDto) {
        EmailMessage emailMessage = EmailMessage.builder()
            .to(emailRequestDto.getEmail())
            .subject("[ToDoTravel] 이메일 인증을 위한 인증 코드 발송")
            .build();

        String code = emailService.sendMail(emailMessage, "email", passwordEncoder);

        EmailResponseDto emailResponseDto = new EmailResponseDto();
        emailResponseDto.setCode(code);

        return new ApiResponse<>(true, "이메일 인증 코드 발송 성공", emailResponseDto);
    }
}
