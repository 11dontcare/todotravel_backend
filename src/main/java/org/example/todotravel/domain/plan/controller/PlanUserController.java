package org.example.todotravel.domain.plan.controller;

import lombok.RequiredArgsConstructor;
import org.example.todotravel.domain.chat.entity.ChatRoom;
import org.example.todotravel.domain.chat.service.impl.ChatRoomServiceImpl;
import org.example.todotravel.domain.plan.entity.PlanUser;
import org.example.todotravel.domain.plan.service.implement.PlanUserServiceImpl;
import org.example.todotravel.global.controller.ApiResponse;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class PlanUserController {
    private final PlanUserServiceImpl planUserService;
    private final ChatRoomServiceImpl chatRoomService;

    // 플랜 초대 거절
    @PutMapping("/invite/{plan_participant_id}/reject")
    public ApiResponse<PlanUser> rejectInvite(@PathVariable("plan_participant_id") Long planParticipantId) {
        PlanUser planUser = planUserService.rejected(planParticipantId);
        return new ApiResponse<>(true, "플랜 초대 거절 성공", planUser);
    }

    // 플랜 초대 수락
    @PutMapping("/invite/{plan_participant_id}/accept")
    public ApiResponse<PlanUser> acceptInvite(@PathVariable("plan_participant_id") Long planParticipantId) {
        // 플랜 초대 수락 처리
        PlanUser planUser = planUserService.accepted(planParticipantId);

        // 채팅방에 자동 참여
        ChatRoom chatRoom = chatRoomService.getChatRoomByPlanId(planUser.getPlan().getPlanId());
        chatRoomService.addUserToChatRoom(chatRoom.getRoomId(), planUser.getUser().getUserId());

        return new ApiResponse<>(true, "플랜 초대 수락 성공", planUser);
    }

    // 플랜 참여자 조회
    @GetMapping("/plan/{plan_id}/participant")
    public ApiResponse<List<PlanUser>> participant(@PathVariable("plan_id") Long planId) {
        List<PlanUser> planUsers = planUserService.getAllPlanUser(planId);
        return new ApiResponse<>(true, "플랜 참여자 조회 성공", planUsers);
    }

    // 플랜 나가기, 초대 취소
    @DeleteMapping("/plan/{plan_id}/participant/{user_id}")
    public ApiResponse<PlanUser> deletePlanUser(@PathVariable("plan_id") Long planId, @PathVariable("user_id") Long userId) {
        // PlanUser에서 해당 사용자 제거
        planUserService.removePlanUser(planId, userId);

        // ChatRoomUser에서도 해당 사용자 제거
        ChatRoom chatRoom = chatRoomService.getChatRoomByPlanId(planId);
        chatRoomService.removeUserFromChatRoom(chatRoom.getRoomId(), userId);

        return new ApiResponse<>(true, "플랜 참여자 삭제 성공");
    }
}
