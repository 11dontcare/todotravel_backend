package org.example.todotravel.domain.chat.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.todotravel.domain.chat.dto.request.FirstUserCheckRequestDto;
import org.example.todotravel.domain.chat.dto.response.ChatRoomListResponseDto;
import org.example.todotravel.domain.chat.dto.response.ChatRoomUserResponseDto;
import org.example.todotravel.domain.chat.entity.ChatRoom;
import org.example.todotravel.domain.chat.entity.ChatRoomUser;
import org.example.todotravel.domain.chat.repository.ChatRoomUserRepository;
import org.example.todotravel.domain.chat.service.ChatMessageService;
import org.example.todotravel.domain.chat.service.ChatRoomUserService;
import org.example.todotravel.domain.user.entity.User;
import org.example.todotravel.global.exception.UserNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChatRoomUserServiceImpl implements ChatRoomUserService {
    private final ChatRoomUserRepository chatRoomUserRepository;
    private final ChatMessageService chatMessageService;

    // 특정 채팅방의 첫 번째 유저인지 판별
    @Override
    @Transactional(readOnly = true)
    public boolean checkFirstUser(FirstUserCheckRequestDto dto) {
        ChatRoomUser chatRoomUser = chatRoomUserRepository.findFirstUserInChatRoom(dto.getRoomId())
            .orElseThrow(() -> new UserNotFoundException("채팅방에 유저가 존재하지 않습니다."));

        return Objects.equals(chatRoomUser.getUser().getUserId(), dto.getUserId());
    }

    // 유저가 가진 채팅방 리스트 조회
    @Override
    @Transactional(readOnly = true)
    public List<ChatRoomListResponseDto> getChatRoomsByUserId(Long userId) {
        List<ChatRoom> chatRooms = chatRoomUserRepository.findChatRoomByUserId(userId);

        return chatRooms.stream()
            .map(chatRoom -> ChatRoomListResponseDto.builder()
                .roomId(chatRoom.getRoomId())
                .roomName(chatRoom.getRoomName())
                .roomDate(chatRoom.getRoomDate())
                .userCount(chatRoom.getChatRoomUsers().size())
                .build())
            .collect(Collectors.toList());
    }

    // 채팅방에 유저 존재 찾기
    @Override
    @Transactional(readOnly = true)
    public ChatRoomUser getChatRoomUserByUserAndRoom(User user, ChatRoom chatRoom) {
        return chatRoomUserRepository.findByUserAndChatRoom(user, chatRoom)
            .orElseThrow(() -> new UserNotFoundException("채팅방에 유저가 존재하지 않습니다."));
    }

    // 채팅방 유저 목록 조회
    @Override
    @Transactional(readOnly = true)
    public List<ChatRoomUserResponseDto> getUsersByRoomId(Long roomId) {
        List<ChatRoomUser> chatRoomUsers = chatRoomUserRepository.findByChatRoomRoomId(roomId);

        return chatRoomUsers.stream()
            .map(chatRoomUser -> ChatRoomUserResponseDto.builder()
                .roomId(chatRoomUser.getChatRoom().getRoomId())
                .userId(chatRoomUser.getUser().getUserId())
                .nickname(chatRoomUser.getUser().getNickname())
                .build())
            .collect(Collectors.toList());
    }

    // 채팅방에 존재하는 사용자인지 확인
    @Override
    @Transactional(readOnly = true)
    public boolean checkUserInChatRoom(ChatRoom chatRoom, User user) {
        return chatRoomUserRepository.existsByChatRoomAndUser(chatRoom, user);
    }

    // 회원 탈퇴 시 특정 채팅방의 사용자 모두 제거
    @Override
    @Transactional
    public void removeAllUserFromChatRoom(ChatRoom chatRoom) {
        chatRoomUserRepository.deleteByChatRoom(chatRoom.getRoomId());
    }

    // 회원 탈퇴 시 채팅방에서 사용자 제거
    @Override
    @Transactional
    public void removeUserFromAllChatRoom(User user) {
        chatRoomUserRepository.deleteByChatRoomUser(user);
    }
}
