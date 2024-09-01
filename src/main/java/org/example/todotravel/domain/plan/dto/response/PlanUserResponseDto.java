package org.example.todotravel.domain.plan.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.todotravel.domain.plan.entity.PlanUser;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class PlanUserResponseDto {
    private Long planParticipantId;
    private PlanUser.StatusType status;
    private Long userId;
    private Long planId;
    private String nickname;
    private String profileImageUrl;

    public static PlanUserResponseDto fromEntity(PlanUser planUser){
        return PlanUserResponseDto.builder()
                .planParticipantId(planUser.getPlanParticipantId())
                .status(planUser.getStatus())
                .userId(planUser.getUser().getUserId())
                .planId(planUser.getPlan().getPlanId())
                .nickname(planUser.getUser().getNickname())
                .profileImageUrl(planUser.getUser().getProfileImageUrl())
                .build();
    }
}
