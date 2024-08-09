package org.example.todotravel.domain.user.service.impl;

import jakarta.mail.internet.MimeMessage;
import org.example.todotravel.domain.user.entity.EmailMessage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EmailServiceImplTest {

    @Mock
    private SpringTemplateEngine templateEngine;

    @Mock
    private JavaMailSender javaMailSender;

    @Mock
    private UserServiceImpl userService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private EmailServiceImpl emailService;

    private EmailMessage emailMessage;

    @BeforeEach
    void setUp() {
        emailMessage = EmailMessage.builder()
            .to("test@example.com")
            .subject("Test Subject")
            .build();
    }

    /**
     * 이메일 전송 테스트에서는 실제로 이메일을 보내지 않고,
     * JavaMailSender의 send 메서드가 호출되었는지만 확인한다.
     * 실제 테스트는 postman이나 연동 시 확인
     */
    @Test
    @DisplayName("이메일 전송 테스트 - 이메일 인증 타입")
    void sendMail_NormalType() {
        // given
        when(javaMailSender.createMimeMessage()).thenReturn(mock(MimeMessage.class));
        when(templateEngine.process(eq("email"), any(Context.class))).thenReturn("Mocked email content");

        // when
        String result = emailService.sendMail(emailMessage, "email", passwordEncoder);

        // then
        assertNotNull(result);
        assertEquals(8, result.length());
        verify(javaMailSender, times(1)).send(any(MimeMessage.class));
    }

    @Test
    @DisplayName("이메일 전송 테스트 - 비밀번호 타입")
    void sendMail_PasswordType() {
        // given
        when(javaMailSender.createMimeMessage()).thenReturn(mock(MimeMessage.class));
        when(templateEngine.process(anyString(), any())).thenReturn("test content");

        // when
        String result = emailService.sendMail(emailMessage, "password", passwordEncoder);

        // then
        assertNotNull(result);
        assertEquals(8, result.length());
        verify(javaMailSender, times(1)).send(any(MimeMessage.class));
        verify(userService, times(1)).setTempPassword(eq("test@example.com"), anyString(), eq(passwordEncoder));
    }

    @Test
    @DisplayName("인증 코드 생성 테스트")
    void createCode() {
        // when
        String code = emailService.createCode();

        // then
        assertNotNull(code);
        assertEquals(8, code.length());
    }

    @Test
    @DisplayName("컨텍스트 설정 테스트")
    void setContext() {
        // given
        when(templateEngine.process(anyString(), any())).thenReturn("test content");

        // when
        String result = emailService.setContext("123456", "email");

        // then
        assertEquals("test content", result);
        verify(templateEngine, times(1)).process(eq("email"), any());
    }
}