package org.example.todotravel.domain.plan.controller;

import lombok.RequiredArgsConstructor;
import org.example.todotravel.domain.chat.entity.ChatRoom;
import org.example.todotravel.domain.chat.service.ChatRoomService;
import org.example.todotravel.domain.plan.dto.response.PlanUserResponseDto;
import org.example.todotravel.domain.plan.entity.Plan;
import org.example.todotravel.domain.plan.entity.PlanUser;
import org.example.todotravel.domain.plan.service.PlanService;
import org.example.todotravel.domain.plan.service.PlanUserService;
import org.example.todotravel.global.controller.ApiResponse;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class PlanUserController {
    private final PlanUserService planUserService;
    private final ChatRoomService chatRoomService;
    private final PlanService planService;

    // 플랜 초대 거절
    @PutMapping("/invite/{plan_participant_id}/reject")
    public ApiResponse<Long> rejectInvite(@PathVariable("plan_participant_id") Long planParticipantId) {
        PlanUser planUser = planUserService.rejected(planParticipantId);
        return new ApiResponse<>(true, "플랜 초대 거절 성공", planUser.getPlanParticipantId());
    }

    // 플랜 초대 수락
    @PutMapping("/invite/{plan_participant_id}/accept")
    public ApiResponse<Long> acceptInvite(@PathVariable("plan_participant_id") Long planParticipantId) {
        // 플랜 초대 수락 처리
        PlanUser planUser = planUserService.accepted(planParticipantId);

        // 채팅방에 자동 참여
        ChatRoom chatRoom = chatRoomService.getChatRoomByPlanId(planUser.getPlan().getPlanId());
        chatRoomService.addUserToChatRoom(chatRoom.getRoomId(), planUser.getUser().getUserId());

        return new ApiResponse<>(true, "플랜 초대 수락 성공", planUser.getPlanParticipantId());
    }

    // 플랜 참여자 조회
    @GetMapping("/plan/{plan_id}/participant")
    public ApiResponse<List<PlanUserResponseDto>> participant(@PathVariable("plan_id") Long planId) {
        List<PlanUser> planUsers = planUserService.getAllPlanUser(planId);
        List<PlanUserResponseDto> planUserList = new ArrayList<>();
        planUsers.forEach(planUser -> {
            planUserList.add(PlanUserResponseDto.fromEntity(planUser));
        });
        return new ApiResponse<>(true, "플랜 참여자 조회 성공", planUserList);
    }

    // 플랜 나가기, 초대 취소
    @DeleteMapping("/plan/{plan_id}/participant/{user_id}")
    public ApiResponse<PlanUser> deletePlanUser(@PathVariable("plan_id") Long planId, @PathVariable("user_id") Long userId) {
        // PlanUser에서 해당 사용자 제거
        planUserService.removePlanUser(planId, userId);

        // ChatRoomUser에서도 해당 사용자 제거
        ChatRoom chatRoom = chatRoomService.getChatRoomByPlanId(planId);
        chatRoomService.removeUserFromChatRoom(chatRoom.getRoomId(), userId);

        //플랜에 참여자가 없으면 플랜 삭제
        if(planUserService.getAllPlanUser(planId) == null){
            chatRoomService.deleteChatRoom(chatRoom.getRoomId());
            Plan plan = planService.getPlan(planId);
            planService.deletePlan(plan);
        }

        return new ApiResponse<>(true, "플랜 참여자 삭제 성공");
    }
}
