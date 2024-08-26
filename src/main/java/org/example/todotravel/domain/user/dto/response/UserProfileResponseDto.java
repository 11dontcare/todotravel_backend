package org.example.todotravel.domain.user.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.example.todotravel.domain.plan.dto.response.PlanListResponseDto;
import org.example.todotravel.domain.user.entity.Gender;

import java.util.List;

@Getter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class UserProfileResponseDto {
    private Long userId;
    private String nickname;
    private Gender gender;
    private int age;
    private String info;
    private String profileImageUrl;
    private boolean isFollowing;
    private int followerCount;
    private int followingCount;
    private int planCount;
    private List<PlanListResponseDto> planList;
}
