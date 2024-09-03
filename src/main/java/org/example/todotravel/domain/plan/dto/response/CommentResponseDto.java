package org.example.todotravel.domain.plan.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.todotravel.domain.plan.entity.Comment;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class CommentResponseDto {
    private Long commentId;
    private Long userId;
    private Long planId;
    private String content;
    private Boolean beforeTravel;
    private String nickname;

    public static CommentResponseDto fromEntity(Comment comment){
        return CommentResponseDto.builder()
                .commentId(comment.getCommentId())
                .userId(comment.getCommentUser().getUserId())
                .planId(comment.getPlan().getPlanId())
                .content(comment.getContent())
                .beforeTravel(comment.getBeforeTravel())
                .nickname(comment.getCommentUser().getNickname())
                .build();
    }
}
