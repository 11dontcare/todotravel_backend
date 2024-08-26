package org.example.todotravel.domain.user.dto.response;

import lombok.Builder;
import lombok.Getter;
import org.example.todotravel.domain.user.entity.SocialType;

import java.time.LocalDateTime;

@Getter
@Builder
public class OAuth2EmailResponseDto {
    private String email;
    private String name;
    private LocalDateTime createdDate;
    private SocialType socialType;
}
