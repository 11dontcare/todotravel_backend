package org.example.todotravel.domain.user.dto.request;

import lombok.Getter;

@Getter
public class PasswordResetRequestDto {
    private Long userId;
    private String newPassword;
}
