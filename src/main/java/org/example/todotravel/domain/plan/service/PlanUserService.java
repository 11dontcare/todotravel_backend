package org.example.todotravel.domain.plan.service;

import org.example.todotravel.domain.plan.dto.response.PlanListResponseDto;
import org.example.todotravel.domain.plan.entity.Plan;
import org.example.todotravel.domain.plan.entity.PlanUser;
import org.example.todotravel.domain.user.dto.response.MyProfileResponseDto;
import org.example.todotravel.domain.user.dto.response.UserProfileResponseDto;
import org.example.todotravel.domain.user.entity.User;

import java.util.List;

public interface PlanUserService {
    PlanUser addPlanUser(Long planId, Long userId);
    PlanUser rejected(Long planParticipantId);
    PlanUser accepted(Long planParticipantId);
    List<PlanUser> getAllPlanUser(Long planId);
    void removePlanUser(Long planId, Long userId);
    void removePlanUserFromOwnPlan(Plan plan);
    void removePlanUserFromPlan(Plan plan, User user);

    UserProfileResponseDto getUserProfile(String subject, User user, boolean isFollowing);
    List<Plan> getAllPlansByUser(User user);
    List<PlanListResponseDto> getAllPlansByUserAndStatus(Long userId);
    List<PlanListResponseDto> getRecentPlansByUser(Long userId);

    Boolean existsPlanUser(Plan plan, Long userId);
}
