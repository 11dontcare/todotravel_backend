package org.example.todotravel.domain.plan.service.implement;

import lombok.RequiredArgsConstructor;
import org.example.todotravel.domain.notification.dto.request.AlarmRequestDto;
import org.example.todotravel.domain.notification.service.AlarmService;
import org.example.todotravel.domain.plan.dto.request.CommentRequestDto;
import org.example.todotravel.domain.plan.entity.Comment;
import org.example.todotravel.domain.plan.entity.Plan;
import org.example.todotravel.domain.plan.repository.CommentRepository;
import org.example.todotravel.domain.plan.service.CommentService;
import org.example.todotravel.domain.user.entity.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {
    private final CommentRepository commentRepository;
    private final AlarmService alarmService; //알림 자동 생성

    @Override
    @Transactional
    public Comment createComment(Plan plan, User user, CommentRequestDto commentRequestDto) {
        Comment comment = Comment.builder()
                .commentUser(user)
                .plan(plan)
                .content(commentRequestDto.getContent())
                .beforeTravel(commentRequestDto.getBeforeTravel())
                .build();
        Comment newComment = commentRepository.save(comment);

        AlarmRequestDto requestDto = new AlarmRequestDto(plan.getPlanUser().getUserId(),
                user.getNickname()+ "님이 [" + plan.getTitle() + "] 플랜에 댓글을 작성했습니다.");
        alarmService.createAlarm(requestDto);

        return newComment;
    }

    @Override
    @Transactional(readOnly = true)
    public Comment getComment(Long commentId) {
        return commentRepository.findById(commentId).orElseThrow(() -> new RuntimeException("댓글을 찾을 수 없습니다."));
    }

    @Override
    @Transactional
    public Comment updateComment(Long commentId, CommentRequestDto commentRequestDto) {
        Comment comment = commentRepository.findById(commentId).orElseThrow(() -> new RuntimeException("댓글을 찾을 수 없습니다."));
        comment = comment.toBuilder()
                .content(commentRequestDto.getContent())
                .beforeTravel(commentRequestDto.getBeforeTravel())
                .build();
        return commentRepository.save(comment);
    }

    @Override
    @Transactional
    public void removeComment(Long commentId) {
        Comment comment = commentRepository.findById(commentId).orElseThrow(() -> new RuntimeException("댓글을 찾을 수 없습니다."));
        commentRepository.delete(comment);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Comment> getCommentsByPlan(Plan plan) {
        return commentRepository.findAllByPlan(plan);
    }
}
