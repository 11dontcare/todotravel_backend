package org.example.todotravel.domain.plan.repository;

import org.example.todotravel.domain.plan.entity.Plan;
import org.example.todotravel.domain.plan.entity.PlanUser;
import org.example.todotravel.domain.plan.entity.Vote;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VoteRepository extends JpaRepository<Vote, Long> {
    Vote findByPlanAndPlanUserAndCategory(Plan plan, PlanUser planUser, Vote.Category category);
    List<Vote> findAllByPlanAndPlanUser(Plan plan, PlanUser planUser);
    Page<Vote> findAllByPlan(Plan plan, Pageable pageable);
}