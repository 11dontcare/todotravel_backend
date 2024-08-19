package org.example.todotravel.domain.user.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PasswordUpdateRequestDto {
    private Long userId;
    private String existingPassword;
    private String newPassword;
}
