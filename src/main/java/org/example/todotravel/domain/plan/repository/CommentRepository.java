package org.example.todotravel.domain.plan.repository;

import org.example.todotravel.domain.plan.entity.Comment;
import org.example.todotravel.domain.plan.entity.Plan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findAllByPlan(Plan plan);
}
