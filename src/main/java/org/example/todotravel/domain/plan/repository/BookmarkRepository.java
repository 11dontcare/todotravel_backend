package org.example.todotravel.domain.plan.repository;

import org.example.todotravel.domain.plan.entity.Bookmark;
import org.example.todotravel.domain.plan.entity.Plan;
import org.example.todotravel.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BookmarkRepository extends JpaRepository<Bookmark, Long> {
    void deleteByPlanAndBookmarkUser(Plan plan, User user);
    Long countByPlan(Plan plan);
    Optional<Bookmark> findByBookmarkUserAndPlan(User user, Plan plan);
}
