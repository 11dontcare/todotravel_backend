package org.example.todotravel.domain.plan.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

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

    private LocalDateTime startDate;

    private LocalDateTime endDate;

    private Long bookmarkNumber;

    private Long likeNumber;
}