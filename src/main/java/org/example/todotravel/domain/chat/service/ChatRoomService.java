package org.example.todotravel.domain.chat.service;

import org.example.todotravel.domain.chat.dto.request.ChatRoomCreateRequestDto;
import org.example.todotravel.domain.chat.dto.request.ChatRoomNameRequestDto;
import org.example.todotravel.domain.chat.dto.request.OneToOneChatRoomRequestDto;
import org.example.todotravel.domain.chat.dto.response.ChatRoomNameResponseDto;
import org.example.todotravel.domain.chat.dto.response.ChatRoomResponseDto;
import org.example.todotravel.domain.chat.entity.ChatRoom;
import org.example.todotravel.domain.plan.entity.Plan;

import java.util.List;

public interface ChatRoomService {
    ChatRoomResponseDto createChatRoom(Plan plan);
    ChatRoomResponseDto createOneToOneChatRoom(OneToOneChatRoomRequestDto dto);
    ChatRoomNameResponseDto updateChatRoomName(ChatRoomNameRequestDto dto);
    void addUserToChatRoom(Long roomId, Long userId);
    ChatRoom getChatRoomByPlanId(Long planId);
    List<ChatRoom> getAllChatRoomByPlan(List<Plan> plans);
    Plan getPlanByRoomId(Long roomId);
    void removeUserFromChatRoom(Long roomId, Long userId);
    void deleteChatRoom(Long roomId);
}
