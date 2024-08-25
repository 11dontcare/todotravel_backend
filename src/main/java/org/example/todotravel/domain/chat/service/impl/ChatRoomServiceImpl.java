package org.example.todotravel.domain.chat.service.impl;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.example.todotravel.domain.chat.dto.request.ChatRoomNameRequestDto;
import org.example.todotravel.domain.chat.dto.request.OneToOneChatRoomRequestDto;
import org.example.todotravel.domain.chat.dto.response.ChatRoomNameResponseDto;
import org.example.todotravel.domain.chat.dto.response.ChatRoomResponseDto;
import org.example.todotravel.domain.chat.entity.ChatRoom;
import org.example.todotravel.domain.chat.entity.ChatRoomUser;
import org.example.todotravel.domain.chat.repository.ChatRoomRepository;
import org.example.todotravel.domain.chat.service.ChatRoomUserService;
import org.example.todotravel.domain.plan.entity.Plan;
import org.example.todotravel.domain.user.entity.User;
import org.example.todotravel.domain.chat.service.ChatRoomService;
import org.example.todotravel.domain.user.service.UserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ChatRoomServiceImpl implements ChatRoomService {
    private final ChatRoomRepository chatRoomRepository;

    private final ChatRoomUserService chatRoomUserService;
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

    // 채팅방 제거
    @Override
    @Transactional
    public void deleteChatRoom(Long roomId) {
        ChatRoom chatRoom = chatRoomRepository.findById(roomId)
            .orElseThrow(() -> new EntityNotFoundException("채팅방을 찾을 수 없습니다."));
        chatRoomRepository.delete(chatRoom);
    }
}
