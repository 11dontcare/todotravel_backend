package org.example.todotravel.domain.plan.service;

import org.example.todotravel.domain.plan.entity.Bookmark;
import org.example.todotravel.domain.plan.entity.Plan;
import org.springframework.stereotype.Service;

@Service
public interface BookmarkService {
    Bookmark createBookmark(Long planId, Long userId);
    void removeBookmark(Long planId, Long userId);
    Long countBookmark(Plan plan);
}
