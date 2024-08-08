package org.example.todotravel.domain.plan.service.implement;

import lombok.RequiredArgsConstructor;
import org.example.todotravel.domain.plan.dto.request.CommentRequestDto;
import org.example.todotravel.domain.plan.entity.Comment;
import org.example.todotravel.domain.plan.entity.Plan;
import org.example.todotravel.domain.plan.repository.CommentRepository;
import org.example.todotravel.domain.plan.service.CommentService;
import org.example.todotravel.domain.user.entity.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {
    private final CommentRepository commentRepository;

    @Override
    @Transactional
    public Comment createComment(Plan plan, User user, CommentRequestDto commentRequestDto) {
        Comment comment = Comment.builder()
                .commentUser(user)
                .plan(plan)
                .content(commentRequestDto.getContent())
                .beforeTravel(commentRequestDto.getBeforeTravel())
                .build();
        return commentRepository.save(comment);
    }

    @Override
    @Transactional(readOnly = true)
    public Comment getComment(Long commentId) {
        return commentRepository.findById(commentId).orElseThrow(() -> new RuntimeException("댓글을 찾을 수 없습니다."));
    }

    @Override
    public Comment updateComment(Long commentId, CommentRequestDto commentRequestDto) {
        Comment comment = commentRepository.findById(commentId).orElseThrow(() -> new RuntimeException("댓글을 찾을 수 없습니다."));
        comment = comment.toBuilder()
                .content(commentRequestDto.getContent())
                .beforeTravel(commentRequestDto.getBeforeTravel())
                .build();
        return commentRepository.save(comment);
    }

    @Override
    public void removeComment(Long commentId) {
        Comment comment = commentRepository.findById(commentId).orElseThrow(() -> new RuntimeException("댓글을 찾을 수 없습니다."));
        commentRepository.delete(comment);
    }
}
