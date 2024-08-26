package org.example.todotravel.domain.plan.repository;

import org.example.todotravel.domain.plan.entity.Plan;
import org.example.todotravel.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PlanRepository extends JpaRepository<Plan, Long> {

    @Modifying
    @Query("DELETE FROM Plan p WHERE p.planId = :planId")
    void deleteByPlanId(@Param("planId") Long planId);

    @Modifying
    @Query("DELETE FROM Plan p WHERE p.planUser = :user")
    void deleteAllByPlanUser(@Param("user") User user);

    Optional<Plan> findByPlanId(Long planId);
    List<Plan> findAllByIsPublicTrue();
    List<Plan> findAllByIsPublicTrueAndTitleContains(String keyword);
    List<Plan> findByPlanUser(User user);
}
