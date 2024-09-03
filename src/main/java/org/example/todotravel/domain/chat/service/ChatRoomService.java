package org.example.todotravel.domain.chat.service;

import org.example.todotravel.domain.chat.dto.request.ChatRoomNameRequestDto;
import org.example.todotravel.domain.chat.dto.request.OneToOneChatRoomRequestDto;
import org.example.todotravel.domain.chat.dto.response.ChatRoomNameResponseDto;
import org.example.todotravel.domain.chat.dto.response.ChatRoomResponseDto;
import org.example.todotravel.domain.chat.entity.ChatRoom;
import org.example.todotravel.domain.plan.entity.Plan;

import java.util.List;

public interface ChatRoomService {

    // 플랜 생성 시 채팅방 생성
    ChatRoomResponseDto createChatRoom(Plan plan);

    // 1:1 채팅방 생성
    ChatRoomResponseDto createOneToOneChatRoom(OneToOneChatRoomRequestDto dto);

    // 채팅방 이름 수정
    ChatRoomNameResponseDto updateChatRoomName(ChatRoomNameRequestDto dto);

    // 채팅방에 사용자 추가
    void addUserToChatRoom(Long roomId, Long userId);

    // planId로 채팅방 찾기
    ChatRoom getChatRoomByPlanId(Long planId);

    // Plan으로 채팅방 찾기
    List<ChatRoom> getAllChatRoomByPlan(List<Plan> plans);

    // roomId로 plan 찾기
    Plan getPlanByRoomId(Long roomId);

    // 채팅방과 플랜에서 사용자 제거
    void removeUserFromChatRoom(Long roomId, Long userId);

    // 채팅방만 제거
    void removeChatRoom(Long roomId);

    // 채팅방과 메시지도 함께 제거
    void removeChatRoomAndMessage(Long roomId);
}
