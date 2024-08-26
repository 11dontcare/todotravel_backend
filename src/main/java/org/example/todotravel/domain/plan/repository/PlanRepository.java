package org.example.todotravel.domain.plan.repository;

import org.example.todotravel.domain.plan.entity.Plan;
import org.example.todotravel.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PlanRepository extends JpaRepository<Plan, Long> {
    void deleteByPlanId(Long planId);
    void deleteAllByPlanUser(User user);
    Optional<Plan> findByPlanId(Long planId);
    List<Plan> findAllByIsPublicTrue();
    List<Plan> findAllByIsPublicTrueAndTitleContains(String keyword);
    List<Plan> findByPlanUser(User user);
}
