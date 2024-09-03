package org.example.todotravel.domain.plan.dto.response;

public interface PlanCountProjection {
    Long getPlanId();
    Long getBookmarkCount();
    Long getLikeCount();
}
