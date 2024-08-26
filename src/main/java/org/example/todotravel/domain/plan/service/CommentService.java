package org.example.todotravel.domain.plan.service;

import org.example.todotravel.domain.plan.dto.request.CommentRequestDto;
import org.example.todotravel.domain.plan.dto.response.CommentSummaryResponseDto;
import org.example.todotravel.domain.plan.entity.Comment;
import org.example.todotravel.domain.plan.entity.Plan;
import org.example.todotravel.domain.user.entity.User;

import java.util.List;

public interface CommentService {
    Comment createComment(Plan plan, User user, CommentRequestDto commentRequestDto);
    Comment getComment(Long commentId);
    Comment updateComment(Long commentId, CommentRequestDto commentRequestDto);
    void removeComment(Long commentId);
    void removeAllCommentByPlan(Plan plan);
    void removeAllCommentByUser(User user);
    List<Comment> getCommentsByPlan(Plan plan);
    List<CommentSummaryResponseDto> getAllCommentedPlansByUser(User user);
    List<CommentSummaryResponseDto> getRecentCommentedPlansByUser(User user);
    void removeAllByPlan(Plan plan);
}
