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
import org.springframework.dao.PessimisticLockingFailureException;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionSystemException;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
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

    // 삭제된 채팅방 ID를 추적하기 위한 Set
    private final Set<Long> deletedChatRoomIds = new HashSet<>();

    private static final int MAX_RETRIES = 3;
    private static final long RETRY_DELAY = 1000; // 1초

    @Override
    @Transactional(timeout = 60) // 60초 타임아웃 설정
    public void withdrawUser(User user) {
        int retries = 0;
        while (retries < MAX_RETRIES) {
            try {
                executeWithdrawal(user);
                return;
            } catch (PessimisticLockingFailureException | ObjectOptimisticLockingFailureException e) {
                log.warn("Lock acquisition failed. Retrying... (Attempt {})", retries + 1, e);
                retries++;
                if (retries < MAX_RETRIES) {
                    try {
                        Thread.sleep(RETRY_DELAY);
                    } catch (InterruptedException ie) {
                        Thread.currentThread().interrupt();
                    }
                }
            } catch (TransactionSystemException e) {
                log.error("Transaction system error during user withdrawal (userId: {})", user.getUserId(), e);
                throw new RuntimeException("회원 탈퇴 중 트랜잭션 오류 발생", e);
            } catch (Exception e) {
                log.error("회원 탈퇴 실패 (userId: {})", user.getUserId(), e);
                throw new RuntimeException("회원 탈퇴 실패", e);
            }
        }
        throw new RuntimeException("회원 탈퇴 실패: 최대 재시도 횟수 초과");
    }

    private void executeWithdrawal(User user) {
        try {
            List<Plan> userPlans = planService.getAllPlanByPlanUser(user);

            CompletableFuture<Void> chatMessageDeletionFuture = CompletableFuture.runAsync(() ->
                removeChatMessages(userPlans).block()
            );

            removeUserData(user, userPlans);
            handleNonCreatedPlans(user);

            chatMessageDeletionFuture.join();

            chatRoomUserService.removeUserFromAllChatRoom(user);
            alarmService.removeAllAlarm(user.getUserId());
            refreshTokenService.removeRefreshToken(user.getUserId());
            followService.removeAllFollowRelationships(user);

            userService.removeUser(user);

            // 짧은 지연 추가
            Thread.sleep(1000);

            // deleted_messages 컬렉션에서 메시지 삭제를 동기적으로 실행
            log.info("Starting to remove deleted messages for rooms: {}", deletedChatRoomIds);
            chatMessageService.removeDeletedMessagesForRooms(deletedChatRoomIds)
                .doOnSuccess(v -> log.info("Successfully removed deleted messages for rooms: {}", deletedChatRoomIds))
                .doOnError(e -> log.error("Error removing deleted messages: ", e))
                .block(); // 작업이 완료될 때까지 기다립니다.
            log.info("Finished removing deleted messages");

        } catch (Exception e) {
            log.error("회원 탈퇴 실패 (userId: {}): {}", user.getUserId(), e.getMessage());
            CompletableFuture.runAsync(() ->
                restoreChatMessages(deletedChatRoomIds).block()
            );
            throw new RuntimeException("회원 탈퇴 실패", e);
        } finally {
            deletedChatRoomIds.clear();
        }
    }

    // 생성한 플랜의 채팅방에 존재하는 메시지 모두 삭제
    private Mono<Void> removeChatMessages(List<Plan> userPlans) {
        return Mono.fromCallable(() -> chatRoomService.getAllChatRoomByPlan(userPlans))
            .flatMapIterable(rooms -> rooms)
            .flatMap(chatRoom -> {
                deletedChatRoomIds.add(chatRoom.getRoomId()); // 삭제되는 채팅방 추적
                return chatMessageService.removeAllMessageForChatRoom(chatRoom.getRoomId());
            })
            .then();
    }

    // 사용자 관련 데이터 삭제 수행
    private void removeUserData(User user, List<Plan> userPlans) {

        // 사용자가 생성한 플랜의 채팅방 삭제
        List<ChatRoom> chatRooms = chatRoomService.getAllChatRoomByPlan(userPlans);
        for (ChatRoom chatRoom : chatRooms) {
            chatRoomUserService.removeAllUserFromChatRoom(chatRoom);
            chatRoomService.removeChatRoom(chatRoom.getRoomId());
        }

        // 사용자가 생성한 플랜에 대해 모두 삭제
        for (Plan plan : userPlans) {
            removeEntirePlan(plan);
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
            removeEntirePlan(plan);

            // 메시지 삭제를 비동기로 처리
            CompletableFuture.runAsync(() ->
                chatMessageService.removeAllMessageForChatRoom(chatRoom.getRoomId()).block()
            );

            chatRoomUserService.removeAllUserFromChatRoom(chatRoom);
            chatRoomService.removeChatRoom(chatRoom.getRoomId());
            deletedChatRoomIds.add(chatRoom.getRoomId()); // 삭제되는 채팅방 추적
        } else {
            updateUserInPlan(user, plan, chatRoom);
            planUserService.removePlanUserFromPlan(plan, user);
        }
    }

    // 참여하고 있는 플랜 중 나머지 인원이 존재할 때
    private void updateUserInPlan(User user, Plan plan, ChatRoom chatRoom) {
        String oldNickname = user.getNickname();
        try {
            chatMessageService.updateNicknameForUser(user.getUserId(), "(알 수 없음)")
                .doOnError(e -> {
                    log.error("Error updating nickname for user ID: {}", user.getUserId(), e);
                    chatMessageService.updateNicknameForUser(user.getUserId(), oldNickname);
                })
                .block();
        } catch (Exception e) {
            log.error("Error in updateUserInPlan for user ID: {}", user.getUserId(), e);
            chatMessageService.restoreNicknameForUser(user.getUserId(), oldNickname).block();
            throw new RuntimeException("Failed to update user in plan", e);
        }
    }

    // 사용자가 생성한 플랜의 댓글, 좋아요, 북마크, 참여자, 일정, 해당 플랜 순서대로 삭제
    protected void removeEntirePlan(Plan plan) {
        commentService.removeAllCommentByPlan(plan);
        likeService.removeAllLikeByPlan(plan);
        bookmarkService.removeAllBookmarksByPlan(plan);
        planUserService.removePlanUserFromOwnPlan(plan);
        scheduleService.removeAllSchedulesByPlan(plan);
        planService.removeJustPlan(plan);
    }

    // 탈퇴 중 오류 발생 시 채팅 메시지 복구
    private Mono<Void> restoreChatMessages(Set<Long> roomIds) {
        return Flux.fromIterable(roomIds)
            .flatMap(chatMessageService::restoreMessagesForChatRoom)
            .then();
    }
}
