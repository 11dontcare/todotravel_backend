package org.example.todotravel.domain.chat.service;

import org.example.todotravel.domain.chat.dto.request.FirstUserCheckRequestDto;
import org.example.todotravel.domain.chat.dto.response.ChatRoomListResponseDto;
import org.example.todotravel.domain.chat.dto.response.ChatRoomUserResponseDto;
import org.example.todotravel.domain.chat.entity.ChatRoom;
import org.example.todotravel.domain.chat.entity.ChatRoomUser;
import org.example.todotravel.domain.user.entity.User;

import java.util.List;

public interface ChatRoomUserService {
    boolean checkFirstUser(FirstUserCheckRequestDto dto);

    List<ChatRoomListResponseDto> getChatRoomsByUserId(Long userId);

    ChatRoomUser getUserByUserId(User user);

    List<ChatRoomUserResponseDto> getUsersByRoomId(Long roomId);

    boolean checkUserInChatRoom(ChatRoom chatRoom, User user);

    void removeAllUserFromChatRoom(ChatRoom chatRoom);

    void removeUserFromAllChatRoom(User user);
}
