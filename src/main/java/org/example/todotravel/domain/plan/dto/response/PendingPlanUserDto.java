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
public class PendingPlanUserDto {
    private Long planParticipantId;
    private String planUserNickname;
    private String pendingUserNickname;
    private String planTitle;
    private Long planId;
    private Boolean recruitment;

    public static PendingPlanUserDto fromEntity(PlanUser planUser){
        return PendingPlanUserDto.builder()
                .planParticipantId(planUser.getPlanParticipantId())
                .planUserNickname(planUser.getPlan().getPlanUser().getNickname())
                .pendingUserNickname(planUser.getUser().getNickname())
                .planTitle(planUser.getPlan().getTitle())
                .planId(planUser.getPlan().getPlanId())
                .recruitment(planUser.getPlan().getRecruitment())
                .build();
    }
}
