package org.example.todotravel.domain.plan.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.todotravel.domain.plan.dto.request.CommentRequestDto;
import org.example.todotravel.domain.plan.dto.response.CommentResponseDto;
import org.example.todotravel.domain.plan.entity.Comment;
import org.example.todotravel.domain.plan.entity.Plan;
import org.example.todotravel.domain.plan.service.implement.CommentServiceImpl;
import org.example.todotravel.domain.plan.service.implement.PlanServiceImpl;
import org.example.todotravel.domain.user.entity.User;
import org.example.todotravel.domain.user.service.impl.UserServiceImpl;
import org.example.todotravel.global.controller.ApiResponse;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/plan")
public class CommentController {
    private final CommentServiceImpl commentService;
    private final PlanServiceImpl planService;
    private final UserServiceImpl userService;

    //댓글 생성
    @PostMapping("{plan_id}/comment/{user_id}")
    public ApiResponse<CommentResponseDto> createComment(@PathVariable("plan_id") Long planId, @PathVariable("user_id") Long userId,
                                                         @Valid @RequestBody CommentRequestDto commentRequestDto){
        Plan plan = planService.getPlan(planId);
        User user = userService.getUserByUserId(userId).orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));
        Comment comment = commentService.createComment(plan, user, commentRequestDto);
        CommentResponseDto commentResponseDto = CommentResponseDto.fromEntity(comment);
        return new ApiResponse<>(true, "댓글 생성 성공", commentResponseDto);
    }

    //수정할 댓글 조회
    @GetMapping("/comment/{comment_id}")
    public ApiResponse<CommentResponseDto> getUpdateComment(@PathVariable("comment_id") Long commentId){
        Comment comment = commentService.getComment(commentId);
        CommentResponseDto commentResponseDto = CommentResponseDto.fromEntity(comment);
        return new ApiResponse<>(true, "수정할 댓글 조회 성공", commentResponseDto);
    }

    //댓글 수정
    @PutMapping("/comment/{comment_id}")
    public ApiResponse<Long> updateComment(@PathVariable("comment_id") Long commentId, @Valid @RequestBody CommentRequestDto commentRequestDto){
        Comment comment = commentService.updateComment(commentId, commentRequestDto);
        return new ApiResponse<>(true, "댓글 수정 성공", comment.getCommentId());
    }

    //댓글 삭제
    @DeleteMapping("/comment/{comment_id}")
    public ApiResponse<Comment> deleteComment(@PathVariable("comment_id") Long commentId){
        commentService.removeComment(commentId);
        return new ApiResponse<>(true, "댓글 삭제 성공");
    }
}
