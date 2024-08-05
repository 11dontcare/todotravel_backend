package org.example.todotravel.domain.plan.service;

import org.example.todotravel.domain.plan.entity.Like;
import org.example.todotravel.domain.plan.entity.Plan;
import org.springframework.stereotype.Service;

@Service
public interface LikeService {
    Like createLike(Long planId, Long userId);
    void removeLike(Long planId, Long userId);
    Long countLike(Plan plan);
}
