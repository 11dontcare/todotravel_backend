package org.example.todotravel.domain.plan.dto.response;

import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class PlanListResponseDto {
    private Long planId;

    private String title;

    private String location;

    private String description;

    private LocalDate startDate;

    private LocalDate endDate;

    private Long bookmarkNumber;

    private Long likeNumber;

    private Integer participantsCount;

    private Long planUserCount;

    private String planUserNickname;

    private String planThumbnailUrl;

    public PlanListResponseDto(Long planId, String title, String location,
                               String description, LocalDate startDate, LocalDate endDate,
                               String planThumbnailUrl, String planUserNickname) {
        this.planId = planId;
        this.title = title;
        this.location = location;
        this.description = description;
        this.startDate = startDate;
        this.endDate = endDate;
        this.planThumbnailUrl = planThumbnailUrl;
        this.planUserNickname = planUserNickname;
    }
}
