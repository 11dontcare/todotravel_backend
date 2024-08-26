package org.example.todotravel.domain.user.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.todotravel.domain.chat.entity.ChatRoom;
import org.example.todotravel.domain.chat.service.ChatMessageService;
import org.example.todotravel.domain.chat.service.ChatRoomService;
import org.example.todotravel.domain.chat.service.ChatRoomUserService;
import org.example.todotravel.domain.notification.service.AlarmService;
import org.example.todotravel.domain.plan.entity.Plan;
import org.example.todotravel.domain.plan.service.*;
import org.example.todotravel.domain.user.entity.User;
import org.example.todotravel.domain.user.service.FollowService;
import org.example.todotravel.domain.user.service.RefreshTokenService;
import org.example.todotravel.domain.user.service.UserService;
import org.example.todotravel.domain.user.service.UserWithdrawalService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserWithdrawalServiceImpl implements UserWithdrawalService {

    private final ChatRoomUserService chatRoomUserService;
    private final RefreshTokenService refreshTokenService;
    private final ChatMessageService chatMessageService;
    private final BookmarkService bookmarkService;
    private final PlanUserService planUserService;
    private final ScheduleService scheduleService;
    private final ChatRoomService chatRoomService;
    private final CommentService commentService;
    private final FollowService followService;
    private final AlarmService alarmService;
    private final LikeService likeService;
    private final PlanService planService;
    private final UserService userService;

    @Override
    @Transactional
    public void withdrawUser(User user) {
        try {
            List<Plan> userPlans = planService.getAllPlanByPlanUser(user);

            // 채팅 메시지 삭제를 비동기로 처리
            CompletableFuture<Void> chatMessageDeletionFuture = CompletableFuture.runAsync(() ->
                deleteChatMessages(userPlans).block()
            );

            // 나머지 작업은 동기적으로 수행
            deleteUserData(user, userPlans);
            handleNonCreatedPlans(user);

            // 채팅 메시지 삭제 완료 대기
            chatMessageDeletionFuture.join();

            // 마지막으로 사용자 제거
            userService.removeUser(user);

        } catch (Exception e) {
            log.error("회원 탈퇴 실패 (userId: {}): {}", user.getUserId(), e.getMessage());
            // MySQL 데이터는 @Transactional 에 의해 자동 롤백
            // 채팅 메시지 복구를 비동기로 처리
            CompletableFuture.runAsync(() ->
                restoreChatMessages(planService.getAllPlanByPlanUser(user)).block()
            );
            throw new RuntimeException("회원 탈퇴 실패", e);
        }
    }

    // 생성한 플랜의 채팅방에 존재하는 메시지 모두 삭제
    private Mono<Void> deleteChatMessages(List<Plan> userPlans) {
        return Mono.fromCallable(() -> chatRoomService.getAllChatRoomByPlan(userPlans))
            .flatMapIterable(rooms -> rooms)
            .flatMap(chatRoom -> chatMessageService.deleteAllMessageForChatRoom(chatRoom.getRoomId()))
            .then();
    }

    // 사용자 관련 데이터 삭제 수행
    private void deleteUserData(User user, List<Plan> userPlans) {
        Long userId = user.getUserId();

        alarmService.deleteAllAlarm(userId);
        refreshTokenService.deleteRefreshToken(userId);
        followService.removeAllFollowRelationships(user);

        // 사용자가 생성한 플랜의 채팅방 삭제
        List<ChatRoom> chatRooms = chatRoomService.getAllChatRoomByPlan(userPlans);
        for (ChatRoom chatRoom : chatRooms) {
            chatRoomUserService.removeAllUserFromChatRoom(chatRoom);
            chatRoomService.deleteChatRoom(chatRoom.getRoomId());
        }

        // 사용자가 생성한 플랜에 대해 모두 삭제
        for (Plan plan : userPlans) {
            deleteEntirePlan(plan);
        }
    }

    // 사용자가 생성하지 않은 플랜에 대한 삭제
    private void handleNonCreatedPlans(User user) {
        commentService.removeAllCommentByUser(user);
        likeService.removeAllLikeByUser(user);
        bookmarkService.removeAllBookmarkByUser(user);

        List<Plan> participatingPlans = planUserService.getAllPlansByUser(user);
        for (Plan plan : participatingPlans) {
            handleParticipatingPlan(user, plan);
        }
    }

    // 참여하고 있는 플랜에 대한 처리
    private void handleParticipatingPlan(User user, Plan plan) {
        ChatRoom chatRoom = chatRoomService.getChatRoomByPlanId(plan.getPlanId());
        if (chatRoom.getChatRoomUsers().size() == 1 || plan.getPlanUsers().size() == 1) {
            deleteEntirePlan(plan);
            chatRoomUserService.removeAllUserFromChatRoom(chatRoom);
            chatRoomService.deleteChatRoom(chatRoom.getRoomId());
        } else {
            updateUserInPlan(user, plan, chatRoom);
        }
    }

    // 참여하고 있는 플랜 중 나머지 인원이 존재할 때
    private void updateUserInPlan(User user, Plan plan, ChatRoom chatRoom) {
        String oldNickname = user.getNickname();
        try {
            chatMessageService.updateNicknameForUser(user.getUserId(), "(알 수 없음)")
                .doOnSuccess(v -> {
                    chatRoomUserService.removeUserFromChatRoom(chatRoom, user);
                    planUserService.removePlanUserFromPlan(plan, user);
                })
                .doOnError(e -> {
                    log.error("Error updating nickname for user ID: {}", user.getUserId(), e);
                    chatMessageService.updateNicknameForUser(user.getUserId(), oldNickname).block();
                })
                .block();
        } catch (Exception e) {
            log.error("Error in updateUserInPlan for user ID: {}", user.getUserId(), e);
            chatMessageService.restoreNicknameForUser(user.getUserId(), oldNickname).block();
            throw new RuntimeException("Failed to update user in plan", e);
        }
    }

    // 사용자가 생성한 플랜의 댓글, 좋아요, 북마크, 참여자, 일정, 해당 플랜 순서대로 삭제
    private void deleteEntirePlan(Plan plan) {
        commentService.removeAllCommentByPlan(plan);
        likeService.removeAllLikeByPlan(plan);
        bookmarkService.removeAllBookmarksByPlan(plan);
        planUserService.removePlanUserFromOwnPlan(plan);
        scheduleService.deleteAllSchedulesByPlan(plan);
        planService.deletePlan(plan.getPlanId());
    }

    // 탈퇴 중 오류 발생 시 채팅 메시지 복구
    private Mono<Void> restoreChatMessages(List<Plan> userPlans) {
        return Mono.fromCallable(() -> chatRoomService.getAllChatRoomByPlan(userPlans))
            .flatMapIterable(rooms -> rooms)
            .flatMap(chatRoom -> chatMessageService.restoreMessagesForChatRoom(chatRoom.getRoomId()))
            .then();
    }
}
