package org.example.todotravel.domain.chat.service;

import org.example.todotravel.domain.chat.dto.request.ChatRoomCreateRequestDto;
import org.example.todotravel.domain.chat.dto.request.ChatRoomNameRequestDto;
import org.example.todotravel.domain.chat.dto.response.ChatRoomNameResponseDto;
import org.example.todotravel.domain.chat.dto.response.ChatRoomResponseDto;

public interface ChatRoomService {
    ChatRoomResponseDto createChatRoom(ChatRoomCreateRequestDto dto);
    ChatRoomNameResponseDto updateChatRoomName(ChatRoomNameRequestDto dto);
    void addUserToChatRoom(Long roomId, Long userId);
    void removeUserFromChatRoom(Long roomId, Long userId);
    void deleteChatRoom(Long roomId);
}
