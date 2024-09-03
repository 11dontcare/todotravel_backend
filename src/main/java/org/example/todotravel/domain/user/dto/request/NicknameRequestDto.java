package org.example.todotravel.domain.user.dto.request;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class NicknameRequestDto {
    private Long userId;

    @Size(max = 8, message = "닉네임은 8자 내로 사용가능합니다.")
    @Pattern(regexp = "^[\\S]+$", message = "닉네임에 공백을 포함할 수 없습니다.")
    private String newNickname;
}
