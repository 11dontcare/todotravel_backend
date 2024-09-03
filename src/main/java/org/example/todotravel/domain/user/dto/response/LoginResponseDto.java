package org.example.todotravel.domain.user.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.todotravel.domain.user.entity.User;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoginResponseDto {
    private Long userId;
    private String nickname;
    private String role;
    private String accessToken;

    public static LoginResponseDto from(User user, String accessToken) {
        return LoginResponseDto.builder()
            .userId(user.getUserId())
            .nickname(user.getNickname())
            .role(user.getRole().name())
            .accessToken(accessToken)
            .build();
    }
}
