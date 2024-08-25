package org.example.todotravel.domain.plan.repository;

import org.example.todotravel.domain.plan.entity.Plan;
import org.example.todotravel.domain.plan.entity.PlanUser;
import org.example.todotravel.domain.user.entity.User;
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

    void deleteAllByPlan(Plan plan);

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
}
