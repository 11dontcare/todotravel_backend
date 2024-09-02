package org.example.todotravel.domain.plan.repository;

import org.example.todotravel.domain.plan.dto.response.PlanListResponseDto;
import org.example.todotravel.domain.plan.entity.Plan;
import org.example.todotravel.domain.plan.entity.PlanUser;
import org.example.todotravel.domain.user.entity.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PlanUserRepository extends JpaRepository<PlanUser, Long> {
    List<PlanUser> findAllByPlan(Plan plan);

    void deletePlanUserByPlanAndUser(Plan plan, User user);

    @Modifying
    @Query("DELETE FROM PlanUser pu WHERE pu.plan = :plan")
    void deleteAllByPlan(@Param("plan") Plan plan);

    @Modifying
    @Query("DELETE FROM PlanUser pu WHERE pu.plan.planId = :planId AND pu.user.userId = :userId")
    void deleteByPlanIdAndUserId(@Param("planId") Long planId, @Param("userId") Long userId);

    @Query("""
        SELECT pu.plan FROM PlanUser pu 
        WHERE pu.user.userId = :userId 
        AND pu.status = :status
        ORDER BY pu.plan.planId DESC
        """)
    List<Plan> findAllPlansByUserId(@Param("userId") Long userId, @Param("status") PlanUser.StatusType status);

    // 타인의 퍼블릭한 플랜 모두 조회
    @Query("""
        SELECT new org.example.todotravel.domain.plan.dto.response.PlanListResponseDto(
            p.planId, p.title, p.location, p.description, p.startDate, p.endDate,
            p.planThumbnailUrl, u.nickname)
        FROM Plan p
        JOIN p.planUser u
        JOIN PlanUser pu ON p.planId = pu.plan.planId
        WHERE pu.user.userId = :userId AND pu.status = :status AND pu.plan.isPublic = true
        ORDER BY p.planId DESC
        """)
    List<PlanListResponseDto> findAllPublicPlanDtosByUserId(@Param("userId") Long userId, @Param("status") PlanUser.StatusType status);

    // 본인이 참여한 모든 플랜 조회
    @Query("""
        SELECT new org.example.todotravel.domain.plan.dto.response.PlanListResponseDto(
            p.planId, p.title, p.location, p.description, p.startDate, p.endDate,
            p.planThumbnailUrl, u.nickname)
        FROM Plan p
        JOIN p.planUser u
        JOIN PlanUser pu ON p.planId = pu.plan.planId
        WHERE pu.user.userId = :userId AND pu.status = :status
        ORDER BY p.planId DESC
        """)
    List<PlanListResponseDto> findAllPlanDtosByUserId(@Param("userId") Long userId, @Param("status") PlanUser.StatusType status);

    @Query("""
        SELECT pu.plan FROM PlanUser pu 
        WHERE pu.user.userId = :userId 
        AND pu.status = :status
        AND pu.plan.recruitment = true
        ORDER BY pu.plan.planId DESC
        LIMIT 4
        """)
    List<Plan> findRecruitingPlansByUserIdLimit4(@Param("userId") Long userId, @Param("status") PlanUser.StatusType status);

    @Query("""
        SELECT pu.plan FROM PlanUser pu 
        WHERE pu.user.userId = :userId 
        AND pu.status = :status
        AND pu.plan.recruitment = true
        ORDER BY pu.plan.planId DESC
        """)
    List<Plan> findAllRecruitingPlansByUserId(@Param("userId") Long userId, @Param("status") PlanUser.StatusType status);

    Boolean existsPlanUserByPlanAndUserAndStatus(Plan plan, User user, PlanUser.StatusType status);
    Boolean existsPlanUserByPlanAndUser(Plan plan, User user);

    @Query("""
        SELECT pu FROM PlanUser pu
        WHERE pu.user.userId = :userId
        AND pu.status = :status
        AND pu.plan.recruitment = false
        ORDER BY pu.planParticipantId DESC
        """)
    List<PlanUser> findAllInvitePlanUserByUserId(@Param("userId") Long userId, @Param("status") PlanUser.StatusType status);

    @Query("""
        SELECT pu FROM PlanUser pu
        WHERE pu.plan.planUser.userId = :userId
        AND pu.plan.recruitment = true
        AND pu.status = :status
        ORDER BY pu.planParticipantId DESC
        """)
    List<PlanUser> findAllRecruitPlanUserByUserId(@Param("userId") Long userId, @Param("status") PlanUser.StatusType status);
}
