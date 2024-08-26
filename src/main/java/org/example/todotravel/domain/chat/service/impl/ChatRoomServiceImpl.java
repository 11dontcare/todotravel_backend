package org.example.todotravel.domain.chat.service.impl;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.todotravel.domain.chat.dto.request.ChatRoomNameRequestDto;
import org.example.todotravel.domain.chat.dto.request.OneToOneChatRoomRequestDto;
import org.example.todotravel.domain.chat.dto.response.ChatRoomNameResponseDto;
import org.example.todotravel.domain.chat.dto.response.ChatRoomResponseDto;
import org.example.todotravel.domain.chat.entity.ChatRoom;
import org.example.todotravel.domain.chat.entity.ChatRoomUser;
import org.example.todotravel.domain.chat.repository.ChatRoomRepository;
import org.example.todotravel.domain.chat.service.ChatMessageService;
import org.example.todotravel.domain.chat.service.ChatRoomUserService;
import org.example.todotravel.domain.plan.entity.Plan;
import org.example.todotravel.domain.user.entity.User;
import org.example.todotravel.domain.chat.service.ChatRoomService;
import org.example.todotravel.domain.user.service.UserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChatRoomServiceImpl implements ChatRoomService {
    private final ChatRoomRepository chatRoomRepository;

    private final ChatRoomUserService chatRoomUserService;
    private final ChatMessageService chatMessageService;
    private final UserService userService;

    // 플랜 생성 시 채팅방 생성
    @Override
    @Transactional
    public ChatRoomResponseDto createChatRoom(Plan plan) {
        ChatRoom chatRoom = ChatRoom.builder()
            .plan(plan)
            .roomName(plan.getTitle())
            .roomDate(LocalDateTime.now())
            .build();
        chatRoom.addUser(plan.getPlanUser());

        ChatRoom savedChatRoom = chatRoomRepository.save(chatRoom);

        return new ChatRoomResponseDto(savedChatRoom);
    }

    // 1:1 채팅방 생성
    @Override
    @Transactional
    public ChatRoomResponseDto createOneToOneChatRoom(OneToOneChatRoomRequestDto dto) {
        User sender = userService.getUserById(dto.getSenderId());
        User receiver = userService.getUserById(dto.getReceiverId());
        String roomName = sender.getNickname() + " - " + receiver.getNickname();

        ChatRoom chatRoom = ChatRoom.builder()
            .roomName(roomName)
            .roomDate(LocalDateTime.now())
            .build();
        chatRoom.addUser(sender);
        chatRoom.addUser(receiver);

        ChatRoom savedChatRoom = chatRoomRepository.save(chatRoom);

        return new ChatRoomResponseDto(savedChatRoom);
    }

    // 채팅방 이름 수정
    @Override
    @Transactional
    public ChatRoomNameResponseDto updateChatRoomName(ChatRoomNameRequestDto dto) {
        ChatRoom chatRoom = chatRoomRepository.findById(dto.getRoomId())
            .orElseThrow(() -> new EntityNotFoundException("채팅방을 찾을 수 없습니다."));
        chatRoom.setRoomName(dto.getNewRoomName());

        ChatRoom updatedChatRoom = chatRoomRepository.save(chatRoom);
        return ChatRoomNameResponseDto.builder()
            .roomId(updatedChatRoom.getRoomId())
            .newRoomName(updatedChatRoom.getRoomName())
            .build();
    }

    // 채팅방에 사용자 추가
    @Override
    @Transactional
    public void addUserToChatRoom(Long roomId, Long userId) {
        ChatRoom chatRoom = chatRoomRepository.findById(roomId)
            .orElseThrow(() -> new EntityNotFoundException("채팅방을 찾을 수 없습니다."));
        User user = userService.getUserById(userId);

        // 사용자가 이미 채팅방에 존재하는지 확인
        boolean userExists = chatRoomUserService.checkUserInChatRoom(chatRoom, user);
        if (userExists) {
            throw new RuntimeException("이미 채팅방에 존재하는 사용자입니다.");
        }

        chatRoom.addUser(user);
        chatRoomRepository.save(chatRoom);
    }

    // planId로 채팅방 찾기
    @Override
    @Transactional(readOnly = true)
    public ChatRoom getChatRoomByPlanId(Long planId) {
        return chatRoomRepository.findByPlanPlanId(planId)
            .orElseThrow(() -> new EntityNotFoundException("채팅방을 찾을 수 없습니다."));
    }

    // Plan으로 채팅방 찾기
    @Override
    @Transactional(readOnly = true)
    public List<ChatRoom> getAllChatRoomByPlan(List<Plan> plans) {
        return chatRoomRepository.findByPlanIn(plans);
    }

    // roomId로 plan 찾기
    @Override
    @Transactional(readOnly = true)
    public Plan getPlanByRoomId(Long roomId) {
        ChatRoom chatRoom = chatRoomRepository.findById(roomId)
            .orElseThrow(() -> new EntityNotFoundException("채팅방을 찾을 수 없습니다."));
        return chatRoom.getPlan();
    }

    // 채팅방과 플랜에서 사용자 제거
    @Override
    @Transactional
    public void removeUserFromChatRoom(Long roomId, Long userId) {
        ChatRoom chatRoom = chatRoomRepository.findById(roomId)
            .orElseThrow(() -> new EntityNotFoundException("채팅방을 찾을 수 없습니다."));
        User user = userService.getUserById(userId);
        ChatRoomUser chatRoomUser = chatRoomUserService.getUserByUserId(user);

        // 채팅방 사용자 제거 후 저장
        chatRoom.getChatRoomUsers().remove(chatRoomUser);
        chatRoomRepository.save(chatRoom);
    }

    // 채팅방만 제거
    @Override
    @Transactional
    public void deleteChatRoom(Long roomId) {
        chatRoomRepository.deleteByRoomId(roomId);
    }

    // 채팅방과 메시지도 함께 제거
    @Override
    @Transactional
    public void deleteChatRoomAndMessage(Long roomId) {
        ChatRoom chatRoom = chatRoomRepository.findById(roomId)
            .orElseThrow(() -> new EntityNotFoundException("채팅방을 찾을 수 없습니다."));

        try {
            // 채팅방의 모든 메시지 삭제
            chatMessageService.deleteAllMessageForChatRoom(roomId)
                .doOnSuccess(v -> log.info("채팅방 {} 의 메시지 삭제 완료", roomId))
                .doOnError(e -> log.error("채팅방 {} 메시지 삭제 중 오류 발생", roomId, e))
                .block(); // Mono를 동기적으로 실행

            // 채팅방 삭제 (JPA에 의해 chat_room_users도 같이 삭제됨)
            chatRoomRepository.delete(chatRoom);
            log.info("채팅방 {} 삭제 완료", roomId);

            // deleted_messages 컬렉션에서 해당 채팅방의 메시지 제거
            chatMessageService.removeDeletedMessagesForRooms(Set.of(roomId))
                .doOnSuccess(v -> log.info("채팅방 {} 의 삭제된 메시지 제거 완료", roomId))
                .doOnError(e -> log.error("채팅방 {} 의 삭제된 메시지 제거 중 오류 발생", roomId, e))
                .block(); // Mono를 동기적으로 실행

        } catch (Exception e) {
            log.error("채팅방 {} 및 메시지 삭제 중 오류 발생", roomId, e);
            // 보상 트랜잭션 실행
            compensateDeleteChatRoomAndMessage(roomId, chatRoom);
            throw new RuntimeException("채팅방 및 메시지 삭제 실패", e);
        }
    }

    // 보상 트랜잭션 메서드
    private void compensateDeleteChatRoomAndMessage(Long roomId, ChatRoom chatRoom) {
        log.info("채팅방 {} 삭제 실패에 대한 보상 트랜잭션 시작", roomId);
        try {
            // ChatMessageService의 restoreMessagesForChatRoom 메서드 호출
            chatMessageService.restoreMessagesForChatRoom(roomId)
                .doOnSuccess(v -> log.info("채팅방 {} 의 메시지 복구 완료", roomId))
                .doOnError(e -> log.error("채팅방 {} 메시지 복구 중 오류 발생", roomId, e))
                .block();

            // 채팅방 재생성 (만약 이미 삭제되었다면)
            if (!chatRoomRepository.existsById(roomId)) {
                chatRoomRepository.save(chatRoom);
                log.info("채팅방 {} 재생성 완료", roomId);
            }

            log.info("채팅방 {} 삭제 실패에 대한 보상 트랜잭션 완료", roomId);
        } catch (Exception e) {
            log.error("채팅방 {} 보상 트랜잭션 실행 중 오류 발생", roomId, e);
            throw new RuntimeException("보상 트랜잭션 실패", e);
        }
    }
}
