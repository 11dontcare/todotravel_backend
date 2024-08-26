package org.example.todotravel.domain.plan.repository;

import org.example.todotravel.domain.plan.dto.response.PlanSummaryDto;
import org.example.todotravel.domain.plan.entity.Like;
import org.example.todotravel.domain.plan.entity.Plan;
import org.example.todotravel.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LikeRepository extends JpaRepository<Like, Long> {
    void deleteByPlanAndLikeUser(Plan plan, User user);
    void deleteAllByLikeUser(User user);
    Long countByPlan(Plan plan);
    Long countByPlanPlanId(Long planId);

    Optional<Like> findByLikeUserAndPlan(User user, Plan plan);

    @Query("SELECT l.plan FROM Like l WHERE l.likeUser.userId = :userId ORDER BY l.likeId DESC")
    List<Plan> findLikedPlansByUserId(@Param("userId") Long userId);

    @Query(nativeQuery = true, value = """
           SELECT p.plan_id as planId, p.title, p.location, p.description, p.start_date as startDate,
                  p.end_date as endDate, u.nickname as planUserNickname
           FROM plans p
           JOIN likes l ON p.plan_id = l.plan_id
           JOIN users u ON p.user_id = u.user_id
           WHERE l.user_id = :userId
           ORDER BY l.like_id DESC
           LIMIT 3
           """)
    List<PlanSummaryDto> findRecentLikedPlansByUserId(@Param("userId") Long userId);

    //플랜 삭제 시 플랜에 달린 좋아요 삭제
    void deleteAllByPlan(Plan plan);
}
