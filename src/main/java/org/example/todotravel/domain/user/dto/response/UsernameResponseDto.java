package org.example.todotravel.domain.user.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class UsernameResponseDto {
    private String username;
    private String name;
    private LocalDateTime createdDate;
}
