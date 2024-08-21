package org.example.todotravel.domain.user.dto.request;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UserInfoRequestDto {
    private Long userId;

    @Size(max = 160, message = "소개글은 160자 제한입니다.")
    private String newInfo;
}
