package org.example.todotravel.domain.plan.service;

import org.example.todotravel.domain.plan.entity.Like;
import org.example.todotravel.domain.plan.entity.Plan;
import org.example.todotravel.domain.user.entity.User;
import org.springframework.stereotype.Service;

@Service
public interface LikeService {
    Like createLike(Plan plan, User user);
    void removeLike(Plan plan, User user);
    Long countLike(Plan plan);
}
