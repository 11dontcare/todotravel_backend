package org.example.todotravel.domain.user.dto.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PasswordSearchResponseDto {
    private Long userId;
    private String code;

    public PasswordSearchResponseDto(Long userId) {
        this.userId = userId;
    }
}
