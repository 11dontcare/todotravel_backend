package org.example.todotravel.domain.plan.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
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
}
