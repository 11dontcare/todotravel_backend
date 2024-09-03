package org.example.todotravel.domain.plan.repository;

import org.example.todotravel.domain.plan.dto.response.PlanListResponseDto;
import org.example.todotravel.domain.plan.dto.response.PlanSummaryDto;
import org.example.todotravel.domain.plan.entity.Bookmark;
import org.example.todotravel.domain.plan.entity.Plan;
import org.example.todotravel.domain.user.entity.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookmarkRepository extends JpaRepository<Bookmark, Long> {
    void deleteByPlanAndBookmarkUser(Plan plan, User user);

    @Modifying
    @Query("DELETE FROM Bookmark b WHERE b.bookmarkUser = :user")
    void deleteAllByBookmarkUser(@Param("user") User user);

    Long countByPlan(Plan plan);

    Long countByPlanPlanId(Long planId);

    Optional<Bookmark> findByBookmarkUserAndPlan(User user, Plan plan);

    @Query("""
        SELECT new org.example.todotravel.domain.plan.dto.response.PlanListResponseDto(
            p.planId, p.title, p.location, p.description, p.startDate, p.endDate,
            p.planThumbnailUrl, pu.nickname)
        FROM Plan p
        JOIN p.planUser pu
        JOIN Bookmark b ON p.planId = b.plan.planId
        WHERE b.bookmarkUser.userId = :userId
        ORDER BY b.bookmarkId DESC
        """)
    List<PlanListResponseDto> findAllBookmarkedPlansByUserId(@Param("userId") Long userId);

    @Query("""
        SELECT new org.example.todotravel.domain.plan.dto.response.PlanListResponseDto(
            p.planId, p.title, p.location, p.description, p.startDate, p.endDate,
            p.planThumbnailUrl, pu.nickname)
        FROM Plan p
        JOIN p.planUser pu
        JOIN Bookmark b ON p.planId = b.plan.planId
        WHERE b.bookmarkUser.userId = :userId
        ORDER BY b.bookmarkId DESC
        """)
    List<PlanListResponseDto> findRecentCommentedPlansByUserId(@Param("userId") Long userId, Pageable pageable);

    //플랜 삭제 시 플랜에 달린 북마크 삭제
    @Modifying
    @Query("DELETE FROM Bookmark b WHERE b.plan = :plan")
    void deleteAllByPlan(@Param("plan") Plan plan);
}
