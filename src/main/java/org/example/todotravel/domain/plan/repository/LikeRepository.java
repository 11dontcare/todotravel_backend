package org.example.todotravel.domain.plan.repository;

import org.example.todotravel.domain.plan.dto.response.PlanListResponseDto;
import org.example.todotravel.domain.plan.dto.response.PlanSummaryDto;
import org.example.todotravel.domain.plan.entity.Like;
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
public interface LikeRepository extends JpaRepository<Like, Long> {
    void deleteByPlanAndLikeUser(Plan plan, User user);

    @Modifying
    @Query("DELETE FROM Like l WHERE l.likeUser = :user")
    void deleteAllByLikeUser(@Param("user") User user);

    Long countByPlan(Plan plan);
    Long countByPlanPlanId(Long planId);

    Optional<Like> findByLikeUserAndPlan(User user, Plan plan);

    @Query("""
        SELECT new org.example.todotravel.domain.plan.dto.response.PlanListResponseDto(
            p.planId, p.title, p.location, p.description, p.startDate, p.endDate,
            p.planThumbnailUrl, pu.nickname)
        FROM Plan p
        JOIN p.planUser pu
        JOIN Like l ON p.planId = l.plan.planId
        WHERE l.likeUser.userId = :userId
        ORDER BY l.likeId DESC
        """)
    List<PlanListResponseDto> findRecentLikedPlanDtosByUserId(@Param("userId") Long userId, Pageable pageable);

    @Query("""
        SELECT new org.example.todotravel.domain.plan.dto.response.PlanListResponseDto(
            p.planId, p.title, p.location, p.description, p.startDate, p.endDate,
            p.planThumbnailUrl, pu.nickname)
        FROM Plan p
        JOIN p.planUser pu
        JOIN Like l ON p.planId = l.plan.planId
        WHERE l.likeUser.userId = :userId
        ORDER BY l.likeId DESC
        """)
    List<PlanListResponseDto> findAllLikedPlanDtosByUserId(@Param("userId") Long userId);

    //플랜 삭제 시 플랜에 달린 좋아요 삭제
    @Modifying
    @Query("DELETE FROM Like l WHERE l.plan = :plan")
    void deleteAllByPlan(@Param("plan") Plan plan);
}
