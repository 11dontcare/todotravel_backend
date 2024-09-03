package org.example.todotravel.domain.user.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.todotravel.domain.user.entity.User;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class UserListResponseDto {
    private Long userId;

    // ID
    private String username;

    // 이름
    private String name;

    // 닉네임
    private String nickname;

    private String profileImageUrl;

    public static UserListResponseDto fromEntity(User user){
        return UserListResponseDto.builder()
                .userId(user.getUserId())
                .username(user.getUsername())
                .name(user.getName())
                .nickname(user.getNickname())
                .profileImageUrl(user.getProfileImageUrl())
                .build();
    }
}
