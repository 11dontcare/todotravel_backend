package org.example.todotravel.domain.plan.repository;

import org.example.todotravel.domain.plan.entity.Plan;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlanRepository extends JpaRepository<Plan, Long> {
}
