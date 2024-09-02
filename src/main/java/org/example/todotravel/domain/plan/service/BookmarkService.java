package org.example.todotravel.domain.plan.service;

import org.example.todotravel.domain.plan.dto.response.PlanListResponseDto;
import org.example.todotravel.domain.plan.dto.response.PlanSummaryDto;
import org.example.todotravel.domain.plan.entity.Bookmark;
import org.example.todotravel.domain.plan.entity.Plan;
import org.example.todotravel.domain.user.entity.User;

import java.util.List;

public interface BookmarkService {
    Bookmark createBookmark(Plan plan, User user);
    void removeBookmark(Plan plan, User user);
    void removeAllBookmarksByPlan(Plan plan);
    void removeAllBookmarkByUser(User user);
    Long countBookmark(Plan plan);
    Long countBookmarkByPlanId(Long planId);
    Boolean isPlanBookmarkedByUser(User user, Plan plan);
    List<PlanListResponseDto> getAllBookmarkedPlansByUser(Long userId);
    List<PlanListResponseDto> getRecentBookmarkedPlansByUser(Long userId);
    void removeAllByPlan(Plan plan);
}
