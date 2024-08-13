package org.example.todotravel.domain.plan.service;

import org.example.todotravel.domain.plan.entity.PlanUser;

import java.util.List;

public interface PlanUserService {
    PlanUser addPlanUser(Long planId, Long userId);
    PlanUser rejected(Long planParticipantId);
    PlanUser accepted(Long planParticipantId);
    List<PlanUser> getAllPlanUser(Long planId);
    void removePlanUser(Long planId, Long userId);
}
