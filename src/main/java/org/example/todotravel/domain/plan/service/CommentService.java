package org.example.todotravel.domain.plan.service;

import org.example.todotravel.domain.plan.dto.request.CommentRequestDto;
import org.example.todotravel.domain.plan.entity.Comment;
import org.example.todotravel.domain.plan.entity.Plan;
import org.example.todotravel.domain.user.entity.User;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface CommentService {
    Comment createComment(Plan plan, User user, CommentRequestDto commentRequestDto);
    Comment getComment(Long commentId);
    Comment updateComment(Long commentId, CommentRequestDto commentRequestDto);
    void removeComment(Long commentId);
    List<Comment> getCommentsByPlan(Plan plan);
}
