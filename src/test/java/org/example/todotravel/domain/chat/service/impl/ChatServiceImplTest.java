package org.example.todotravel.domain.chat.service.impl;

import org.example.todotravel.domain.chat.dto.response.ChatRoomResponseDto;
import org.example.todotravel.domain.chat.entity.ChatRoom;
import org.example.todotravel.domain.chat.repository.ChatRoomRepository;
import org.example.todotravel.domain.plan.entity.Plan;
import org.example.todotravel.domain.plan.repository.PlanRepository;
import org.example.todotravel.domain.user.entity.User;
import org.example.todotravel.domain.user.repository.UserRepository;
import org.example.todotravel.domain.chat.service.ChatService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ChatServiceImplTest {

    @Mock
    private ChatRoomRepository chatRoomRepository;

    @Mock
    private PlanRepository planRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private ChatServiceImpl chatServiceImpl;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createChatRoom() {
        Long userId = 1L;
        Long planId = 1L;
        String roomName = "Test Room";

        User user = new User();
        user.setUserId(userId);
        Plan plan = new Plan();
        plan.setPlanId(planId);

        when(userRepository.findById(userId)).thenReturn(java.util.Optional.of(user));
        when(planRepository.findById(planId)).thenReturn(java.util.Optional.of(plan));

        ChatRoomResponseDto chatRoomResponseDto = chatServiceImpl.createChatRoom(userId, planId, roomName);

        assertNotNull(chatRoomResponseDto);
        assertEquals(roomName, chatRoomResponseDto.getRoomName());
        verify(chatRoomRepository, times(1)).save(any(ChatRoom.class));
    }

    @Test
    void updateChatRoomName() {
        Long roomId = 1L;
        String newName = "Updated Room";

        ChatRoom chatRoom = new ChatRoom();
        chatRoom.setRoomId(roomId);
        chatRoom.setRoomName("Old Room");

        when(chatRoomRepository.findById(roomId)).thenReturn(java.util.Optional.of(chatRoom));

        chatServiceImpl.updateChatRoomName(roomId, newName);

        assertEquals(newName, chatRoom.getRoomName());
        verify(chatRoomRepository, times(1)).save(chatRoom);
    }

    @Test
    void addUserToChatRoom() {
        Long roomId = 1L;
        Long userId = 1L;

        ChatRoom chatRoom = new ChatRoom();
        chatRoom.setRoomId(roomId);

        User user = new User();
        user.setUserId(userId);

        when(chatRoomRepository.findById(roomId)).thenReturn(java.util.Optional.of(chatRoom));
        when(userRepository.findById(userId)).thenReturn(java.util.Optional.of(user));

        chatServiceImpl.addUserToChatRoom(roomId, userId);

        assertEquals(user, chatRoom.getChatUser());
        verify(chatRoomRepository, times(1)).save(chatRoom);
    }

    @Test
    void removeUserFromChatRoom() {
        Long roomId = 1L;
        Long userId = 1L;

        ChatRoom chatRoom = new ChatRoom();
        chatRoom.setRoomId(roomId);

        User user = new User();
        user.setUserId(userId);

        when(chatRoomRepository.findById(roomId)).thenReturn(java.util.Optional.of(chatRoom));
        when(userRepository.findById(userId)).thenReturn(java.util.Optional.of(user));

        chatServiceImpl.removeUserFromChatRoom(roomId, userId);

        assertNull(chatRoom.getChatUser());
        verify(chatRoomRepository, times(1)).save(chatRoom);
    }
}
