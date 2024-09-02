package org.example.todotravel.domain.chat.service;

import org.example.todotravel.domain.chat.dto.request.FirstUserCheckRequestDto;
import org.example.todotravel.domain.chat.dto.response.ChatRoomListResponseDto;
import org.example.todotravel.domain.chat.dto.response.ChatRoomUserResponseDto;
import org.example.todotravel.domain.chat.entity.ChatRoom;
import org.example.todotravel.domain.chat.entity.ChatRoomUser;
import org.example.todotravel.domain.user.entity.User;

import java.util.List;

public interface ChatRoomUserService {

    // 특정 채팅방의 첫 번째 유저인지 판별
    boolean checkFirstUser(FirstUserCheckRequestDto dto);

    // 유저가 가진 채팅방 리스트 조회
    List<ChatRoomListResponseDto> getChatRoomsByUserId(Long userId);

    // 채팅방에 유저 존재 찾기
    ChatRoomUser getChatRoomUserByUserAndRoom(User user, ChatRoom chatRoom);

    // 채팅방 유저 목록 조회
    List<ChatRoomUserResponseDto> getUsersByRoomId(Long roomId);

    // 채팅방에 존재하는 사용자인지 확인
    boolean checkUserInChatRoom(ChatRoom chatRoom, User user);

    // 회원 탈퇴 시 특정 채팅방의 사용자 모두 제거
    void removeAllUserFromChatRoom(ChatRoom chatRoom);

    // 회원 탈퇴 시 채팅방에서 사용자 제거
    void removeUserFromAllChatRoom(User user);
}
