package org.example.todotravel.domain.user.controller;

import lombok.RequiredArgsConstructor;
import org.example.todotravel.domain.user.dto.request.EmailRequestDto;
import org.example.todotravel.domain.user.dto.request.PasswordSearchRequestDto;
import org.example.todotravel.domain.user.dto.request.UsernameRequestDto;
import org.example.todotravel.domain.user.dto.response.EmailResponseDto;
import org.example.todotravel.domain.user.dto.response.PasswordSearchResponseDto;
import org.example.todotravel.domain.user.entity.EmailMessage;
import org.example.todotravel.domain.user.service.EmailService;
import org.example.todotravel.domain.user.service.UserService;
import org.example.todotravel.global.controller.ApiResponse;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 이메일 인증 요청을 다루는 컨트롤러
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/send-mail")
public class EmailController {
    private final UserService userService;
    private final EmailService emailService;
    private final PasswordEncoder passwordEncoder;

    // 이메일 인증 - 요청 시 body로 인증번호 반환
    @PostMapping("/email")
    public ApiResponse<?> sendJoinMail(@RequestBody EmailRequestDto dto) {
        EmailMessage emailMessage = setEmailMessage(dto.getEmail());
        String code = emailService.sendMail(emailMessage, "email", passwordEncoder);

        EmailResponseDto emailResponseDto = new EmailResponseDto();
        emailResponseDto.setCode(code);

        return new ApiResponse<>(true, "인증번호가 발송되었습니다. 이메일을 확인해주세요.", emailResponseDto);
    }

    // 이메일 인증 - 이름, 이메일에 해당하는 유저 찾은 후 이메일 전송
    @PostMapping("/find-username")
    public ApiResponse<?> sendMailToFindId(@RequestBody UsernameRequestDto dto) {
        // 입력 정보와 일치하는 회원이 있는지 검사 (여기서 반환은 x)
        userService.getUsernameOrEmail(dto);

        EmailMessage emailMessage = setEmailMessage(dto.getEmail());
        String code = emailService.sendMail(emailMessage, "email", passwordEncoder);

        EmailResponseDto emailResponseDto = new EmailResponseDto();
        emailResponseDto.setCode(code);

        return new ApiResponse<>(true, "인증번호가 발송되었습니다. 이메일을 확인해주세요.", emailResponseDto);
    }

    // 이메일 인증 - 이름, 생년월일, 이메일에 해당하는 유저 찾은 후 이메일 전송
    @PostMapping("/find-password")
    public ApiResponse<?> sendMailToFindPassword(@RequestBody PasswordSearchRequestDto dto) {
        PasswordSearchResponseDto passwordSearchResponseDto = userService.getUserByNameAndBirthAndEmail(dto);

        EmailMessage emailMessage = setEmailMessage(dto.getEmail());
        String code = emailService.sendMail(emailMessage, "email", passwordEncoder);

        passwordSearchResponseDto.setCode(code);
        return new ApiResponse<>(true, "인증번호가 발송되었습니다. 이메일을 확인해주세요.", passwordSearchResponseDto);
    }

    private EmailMessage setEmailMessage(String email) {
        return EmailMessage.builder()
            .to(email)
            .subject("[ToDoTravel] 이메일 인증을 위한 인증 코드 발송")
            .build();
    }
}
