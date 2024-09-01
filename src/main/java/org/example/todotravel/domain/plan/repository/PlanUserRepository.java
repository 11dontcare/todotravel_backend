package org.example.todotravel.domain.plan.repository;

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

    @Query("""
        SELECT pu.plan FROM PlanUser pu 
        WHERE pu.user.userId = :userId 
        AND pu.status = :status
        ORDER BY pu.plan.planId DESC
        """)
    List<Plan> findAllPlansByUserIdTop4(@Param("userId") Long userId, @Param("status") PlanUser.StatusType status, Pageable pageable);

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
