package org.example.todotravel.domain.plan.service;

import org.example.todotravel.domain.plan.entity.Bookmark;
import org.example.todotravel.domain.plan.entity.Plan;
import org.example.todotravel.domain.user.entity.User;

public interface BookmarkService {
    Bookmark createBookmark(Plan plan, User user);
    void removeBookmark(Plan plan, User user);
    Long countBookmark(Plan plan);
    Boolean isPlanBookmarkedByUser(User user, Plan plan);
}
