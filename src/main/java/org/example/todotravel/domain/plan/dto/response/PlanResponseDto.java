package org.example.todotravel.domain.plan.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.todotravel.domain.plan.entity.Plan;

import java.time.LocalDateTime;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder(toBuilder = true)
public class PlanResponseDto {
    private Long planId;

    private String title;

    private String location;

    private String description;

    private LocalDateTime startDate;

    private LocalDateTime endDate;

    private Boolean isPublic;

    private Boolean status;

    private Integer participantsCount;

    private Long totalBudget;

    private Long PlanUserId;

    private Long bookmarkNumber;

    private Long likeNumber;

//    private List<ScheduleResponseDto> scheduleList;

    public static PlanResponseDto fromEntity(Plan plan){
        return PlanResponseDto.builder()
                .planId(plan.getPlanId())
                .title(plan.getTitle())
                .location(plan.getLocation())
                .description(plan.getDescription())
                .startDate(plan.getStartDate())
                .endDate(plan.getEndDate())
                .isPublic(plan.getIsPublic())
                .status(plan.getStatus())
                .participantsCount(plan.getParticipantsCount())
                .totalBudget(plan.getTotalBudget())
                .PlanUserId(plan.getPlanUser().getUserId())
                .build();
    }

    //일정이랑 댓글
}
