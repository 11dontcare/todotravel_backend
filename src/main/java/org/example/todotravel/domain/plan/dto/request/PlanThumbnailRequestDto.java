package org.example.todotravel.domain.plan.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PlanThumbnailRequestDto {
    private Long planId;
    private String planThumbnailUrl;
}
