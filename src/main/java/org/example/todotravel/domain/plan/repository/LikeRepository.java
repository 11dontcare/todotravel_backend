package org.example.todotravel.domain.plan.repository;

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

    @Query("SELECT l.plan FROM Like l WHERE l.likeUser.userId = :userId ORDER BY l.likeId DESC")
    List<Plan> findLikedPlansByUserId(@Param("userId") Long userId);

    @Query("""
           SELECT p FROM Plan p
           JOIN Like l ON l.plan.planId = p.planId
           WHERE l.likeUser.userId = :userId
           ORDER BY l.likeId DESC
           """)
    List<Plan> findRecentLikedPlansByUserId(@Param("userId") Long userId, Pageable pageable);

    //플랜 삭제 시 플랜에 달린 좋아요 삭제
    @Modifying
    @Query("DELETE FROM Like l WHERE l.plan = :plan")
    void deleteAllByPlan(@Param("plan") Plan plan);
}
