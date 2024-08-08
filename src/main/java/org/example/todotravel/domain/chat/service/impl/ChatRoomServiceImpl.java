package org.example.todotravel.domain.chat.service.impl;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.example.todotravel.domain.chat.dto.request.ChatRoomCreateRequestDto;
import org.example.todotravel.domain.chat.dto.request.ChatRoomNameRequestDto;
import org.example.todotravel.domain.chat.dto.response.ChatRoomNameResponseDto;
import org.example.todotravel.domain.chat.dto.response.ChatRoomResponseDto;
import org.example.todotravel.domain.chat.entity.ChatRoom;
import org.example.todotravel.domain.chat.entity.ChatRoomUser;
import org.example.todotravel.domain.chat.repository.ChatRoomRepository;
import org.example.todotravel.domain.plan.entity.Plan;
import org.example.todotravel.domain.plan.service.implement.PlanServiceImpl;
import org.example.todotravel.domain.user.entity.User;
import org.example.todotravel.domain.chat.service.ChatRoomService;
import org.example.todotravel.domain.user.service.impl.UserServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ChatRoomServiceImpl implements ChatRoomService {
    private final ChatRoomRepository chatRoomRepository;
    private final ChatRoomUserServiceImpl chatRoomUserService;
    private final PlanServiceImpl planService;
    private final UserServiceImpl userService;

    // 플랜 생성 시 채팅방 생성
    @Override
    @Transactional
    public ChatRoomResponseDto createChatRoom(ChatRoomCreateRequestDto dto) {
        User user = userService.getUserById(dto.getUserId());
        Plan plan = planService.getPlan(dto.getPlanId());

        ChatRoom chatRoom = ChatRoom.builder()
            .plan(plan)
            .roomName(dto.getRoomName())
            .roomDate(LocalDateTime.now())
            .build();
        chatRoom.addUser(user);

        chatRoomRepository.save(chatRoom);

        return ChatRoomResponseDto.builder()
            .roomId(chatRoom.getRoomId())
            .roomName(chatRoom.getRoomName())
            .build();
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

        chatRoom.addUser(user);
        chatRoomRepository.save(chatRoom);
    }

    // 채팅방에서 사용자 제거
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
