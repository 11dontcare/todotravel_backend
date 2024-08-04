package org.example.todotravel.domain.chat.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.todotravel.domain.chat.dto.response.ChatRoomResponseDto;
import org.example.todotravel.domain.chat.entity.ChatRoom;
import org.example.todotravel.domain.chat.repository.ChatRoomRepository;
import org.example.todotravel.domain.plan.entity.Plan;
import org.example.todotravel.domain.plan.repository.PlanRepository;
import org.example.todotravel.domain.user.entity.User;
import org.example.todotravel.domain.user.repository.UserRepository;
import org.example.todotravel.domain.chat.service.ChatService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChatServiceImpl implements ChatService {

    private final ChatRoomRepository chatRoomRepository;
    private final PlanRepository planRepository;
    private final UserRepository userRepository;

    @Override
    public ChatRoomResponseDto createChatRoom(Long userId, Long planId, String roomName) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid user ID"));
        Plan plan = planRepository.findById(planId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid plan ID"));

        ChatRoom chatRoom = new ChatRoom();
        chatRoom.setChatUser(user);
        chatRoom.setPlan(plan);
        chatRoom.setRoomName(roomName);

        chatRoomRepository.save(chatRoom);

        return ChatRoomResponseDto.builder()
                .roomId(chatRoom.getRoomId())
                .roomName(chatRoom.getRoomName())
                .build();
    }

    @Override
    public void updateChatRoomName(Long roomId, String newName) {
        ChatRoom chatRoom = chatRoomRepository.findById(roomId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid room ID"));
        chatRoom.setRoomName(newName);
        chatRoomRepository.save(chatRoom);
    }

    @Override
    public void addUserToChatRoom(Long roomId, Long userId) {
        ChatRoom chatRoom = chatRoomRepository.findById(roomId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid room ID"));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid user ID"));
        chatRoom.setChatUser(user);
        chatRoomRepository.save(chatRoom);
    }

    @Override
    public void removeUserFromChatRoom(Long roomId, Long userId) {
        ChatRoom chatRoom = chatRoomRepository.findById(roomId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid room ID"));
        chatRoom.setChatUser(null);
        chatRoomRepository.save(chatRoom);
    }
}
