package org.example.todotravel.domain.user.service;

import org.example.todotravel.domain.user.entity.EmailMessage;
import org.springframework.security.crypto.password.PasswordEncoder;

public interface EmailService {
    String sendMail(EmailMessage emailMessage, String type, PasswordEncoder passwordEncoder);

    String createCode();

    String setContext(String code, String type);
}
