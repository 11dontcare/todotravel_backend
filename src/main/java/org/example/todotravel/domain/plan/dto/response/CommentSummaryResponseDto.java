package org.example.todotravel.domain.plan.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CommentSummaryResponseDto {
    private Long planId;
    private String title;
    private String location;
    private String content;
    private String planThumbnailUrl;
}
