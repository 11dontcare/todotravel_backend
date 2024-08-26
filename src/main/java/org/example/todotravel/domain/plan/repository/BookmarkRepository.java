package org.example.todotravel.domain.plan.repository;

import org.example.todotravel.domain.plan.dto.response.PlanSummaryDto;
import org.example.todotravel.domain.plan.entity.Bookmark;
import org.example.todotravel.domain.plan.entity.Plan;
import org.example.todotravel.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookmarkRepository extends JpaRepository<Bookmark, Long> {
    void deleteByPlanAndBookmarkUser(Plan plan, User user);
    void deleteAllByBookmarkUser(User user);
    Long countByPlan(Plan plan);
    Long countByPlanPlanId(Long planId);
    Optional<Bookmark> findByBookmarkUserAndPlan(User user, Plan plan);

    @Query("SELECT b.plan FROM Bookmark b WHERE b.bookmarkUser.userId = :userId ORDER BY b.bookmarkId DESC")
    List<Plan> findBookmarkedPlansByUserId(@Param("userId") Long userId);

    @Query(nativeQuery = true, value = """
        SELECT p.plan_id as planId, p.title, p.location, p.description, p.start_date as startDate,
               p.end_date as endDate, u.nickname as planUserNickname
        FROM plans p
        JOIN bookmarks b ON p.plan_id = b.plan_id
        JOIN users u ON p.user_id = u.user_id
        WHERE b.user_id = :userId
        ORDER BY b.bookmark_id DESC
        LIMIT 3
        """)
    List<PlanSummaryDto> findRecentCommentedPlansByUserId(@Param("userId") Long userId);

    //플랜 삭제 시 플랜에 달린 북마크 삭제
    void deleteAllByPlan(Plan plan);
}
