package org.example.todotravel.domain.chat.service;

import org.example.todotravel.domain.chat.dto.response.ChatRoomResponseDto;

public interface ChatService {
    ChatRoomResponseDto createChatRoom(Long userId, Long planId, String roomName);
    void updateChatRoomName(Long roomId, String newName);
    void addUserToChatRoom(Long roomId, Long userId);
    void removeUserFromChatRoom(Long roomId, Long userId);
}
