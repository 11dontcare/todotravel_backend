package org.example.todotravel.domain.plan.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.todotravel.domain.plan.entity.Comment;
import org.example.todotravel.domain.plan.entity.Plan;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class PlanResponseDto {
    private Plan plan;
    private Long bookmarkNumber;
    private Long likeNumber;
    private List<Comment> comments;
}
