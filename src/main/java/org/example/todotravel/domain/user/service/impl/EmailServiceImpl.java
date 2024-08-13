package org.example.todotravel.domain.user.service.impl;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.todotravel.domain.user.entity.EmailMessage;
import org.example.todotravel.domain.user.service.EmailService;
import org.example.todotravel.domain.user.service.UserService;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.util.Random;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {
    private final SpringTemplateEngine templateEngine;
    private final JavaMailSender javaMailSender;
    private final UserService userService;

    // 이메일 전송 메서드
    @Override
    public String sendMail(EmailMessage emailMessage, String type, PasswordEncoder passwordEncoder) {
        String authNum = createCode();

        MimeMessage mimeMessage = javaMailSender.createMimeMessage();

        try {
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, false, "UTF-8");
            mimeMessageHelper.setTo(emailMessage.getTo()); // 메일 수신자
            mimeMessageHelper.setSubject(emailMessage.getSubject()); // 메일 제목
            mimeMessageHelper.setText(setContext(authNum, type), true); // 메일 본문 내용, HTML 여부
            javaMailSender.send(mimeMessage);

            log.info("{} :: 메일 전송 성공", emailMessage.getTo());

            return authNum;
        } catch (MessagingException e) {
            throw new RuntimeException("메일 전송 실패", e);
        }
    }

    // 인증번호 생성 메서드
    @Override
    public String createCode() {
        Random random = new Random();
        StringBuffer key = new StringBuffer();

        for (int i = 0; i < 6; i++) {
            key.append(random.nextInt(10));
        }

        return key.toString();
    }

    // thymeleaf를 통한 html 적용
    @Override
    public String setContext(String code, String type) {
        Context context = new Context();
        context.setVariable("code", code);
        return templateEngine.process(type, context);
    }
}
