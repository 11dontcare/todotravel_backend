package org.example.todotravel.domain.user.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.todotravel.domain.user.entity.Gender;
import org.example.todotravel.domain.user.entity.SocialType;
import org.example.todotravel.domain.user.entity.User;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserDetailResponseDto {
    private String username;
    private String email;
    private String name;
    private String nickname;
    private Gender gender;
    private LocalDateTime createdDate;
    private LocalDate birthDate;
    private SocialType socialType;

    public static UserDetailResponseDto from(User user) {
        return UserDetailResponseDto.builder()
            .username(user.getUsername())
            .email(user.getEmail())
            .name(user.getName())
            .nickname(user.getNickname())
            .gender(user.getGender())
            .createdDate(user.getCreatedDate())
            .birthDate(user.getBirthDate())
            .socialType(user.getSocialType())
            .build();
    }
}
