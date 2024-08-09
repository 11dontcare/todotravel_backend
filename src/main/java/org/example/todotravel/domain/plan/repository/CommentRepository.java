package org.example.todotravel.domain.plan.repository;

import org.example.todotravel.domain.plan.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
}
