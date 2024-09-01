package org.example.todotravel.domain.plan.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.todotravel.domain.plan.entity.Plan;
import org.example.todotravel.domain.plan.entity.PlanUser;

import java.time.LocalDate;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder(toBuilder = true)
public class PlanResponseDto {
    private Long planId;

    private String title;

    private String frontLocation;

    private String location;

    private String description;

    private LocalDate startDate;

    private LocalDate endDate;

    private Boolean isPublic;

    private Boolean status;

    private String planThumbnailUrl;

    private Integer participantsCount;

    private Long planUserCount;

    private Boolean recruitment;

    private Long totalBudget;

    private Long planUserId;

    private String planUserNickname;

    private Long bookmarkNumber;

    private Long likeNumber;

    private List<ScheduleResponseDto> scheduleList;

    private List<CommentResponseDto> commentList;

    public static PlanResponseDto fromEntity(Plan plan){
        return PlanResponseDto.builder()
                .planId(plan.getPlanId())
                .title(plan.getTitle())
                .frontLocation(plan.getFrontLocation())
                .location(plan.getLocation())
                .description(plan.getDescription())
                .startDate(plan.getStartDate())
                .endDate(plan.getEndDate())
                .isPublic(plan.getIsPublic())
                .status(plan.getStatus())
                .planThumbnailUrl(plan.getPlanThumbnailUrl())
                .participantsCount(plan.getParticipantsCount())
                .planUserCount(plan.getPlanUsers().stream().filter(planUser -> planUser.getStatus() == PlanUser.StatusType.ACCEPTED).count())
                .recruitment(plan.getRecruitment())
                .totalBudget(plan.getTotalBudget())
                .planUserId(plan.getPlanUser().getUserId())
                .planUserNickname(plan.getPlanUser().getNickname())
                .build();
    }
}
