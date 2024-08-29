package org.example.todotravel.domain.plan.repository;

import org.example.todotravel.domain.plan.dto.response.CommentSummaryResponseDto;
import org.example.todotravel.domain.plan.entity.Comment;
import org.example.todotravel.domain.plan.entity.Plan;
import org.example.todotravel.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findAllByPlan(Plan plan);

    @Modifying
    @Query("DELETE FROM Comment c WHERE c.commentUser = :user")
    void deleteAllByCommentUserWithQuery(@Param("user") User user);

    @Query("""
        SELECT NEW org.example.todotravel.domain.plan.dto.response.CommentSummaryResponseDto(
            c.plan.planId,
            c.plan.title,
            c.plan.location,
            (SELECT c2.content FROM Comment c2
             WHERE c2.plan = c.plan
             AND c2.commentId = (SELECT MAX(c3.commentId) FROM Comment c3 WHERE c3.plan = c.plan)),
             c.plan.planThumbnailUrl
        )
        FROM Comment c
        WHERE c.commentUser.userId = :userId
        GROUP BY c.plan
        ORDER BY MAX(c.commentId) DESC
        """)
    List<CommentSummaryResponseDto> findDistinctPlansByUserIdOrderByLatestComment(@Param("userId") Long userId);

    @Query("""
        SELECT NEW org.example.todotravel.domain.plan.dto.response.CommentSummaryResponseDto(
            c.plan.planId,
            c.plan.title,
            c.plan.location,
            c.content,
            c.plan.planThumbnailUrl
        )
        FROM Comment c
        WHERE c.commentUser.userId = :userId
        AND c.commentId IN (
            SELECT MAX(c2.commentId)
            FROM Comment c2
            WHERE c2.commentUser.userId = :userId
            GROUP BY c2.plan
            ORDER BY MAX(c2.commentId) DESC
        )
        ORDER BY c.commentId DESC
        LIMIT 4
        """)
    List<CommentSummaryResponseDto> findTop4DistinctPlansByUserIdOrderByLatestComment(@Param("userId") Long userId);

    // 플랜 삭제 시 플랜에 달린 댓글 삭제
    @Modifying
    @Query("DELETE FROM Comment c WHERE c.plan = :plan")
    void deleteAllByPlan(@Param("plan") Plan plan);
}
