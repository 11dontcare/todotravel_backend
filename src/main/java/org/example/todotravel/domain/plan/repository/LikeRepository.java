package org.example.todotravel.domain.plan.repository;

import org.example.todotravel.domain.plan.entity.Like;
import org.example.todotravel.domain.plan.entity.Plan;
import org.example.todotravel.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LikeRepository extends JpaRepository<Like, Long> {
    void deleteByPlanAndLikeUser(Plan plan, User user);
    Long countByPlan(Plan plan);
}
