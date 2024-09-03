package org.example.todotravel.domain.user.dto.request;

import lombok.Getter;

@Getter
public class PasswordResetRequestDto {
    private Long userId;

    // 통합 테스트 시 추가 필요
//    @Size(min = 8, max = 20, message = "비밀번호는 8자 이상 20자 이하여야 합니다.")
//    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$",
//        message = "비밀번호는 대소문자, 숫자, 특수문자를 포함해야 합니다.")
    private String newPassword;
}
