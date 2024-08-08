package org.example.todotravel.domain.chat.service.impl;

import org.example.todotravel.domain.chat.dto.response.ChatRoomResponseDto;
import org.example.todotravel.domain.chat.entity.ChatRoom;
import org.example.todotravel.domain.chat.repository.ChatRoomRepository;
import org.example.todotravel.domain.plan.entity.Plan;
import org.example.todotravel.domain.plan.repository.PlanRepository;
import org.example.todotravel.domain.user.entity.User;
import org.example.todotravel.domain.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

class ChatRoomServiceImplTest {

    @Mock
    private ChatRoomRepository chatRoomRepository;

    @Mock
    private PlanRepository planRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private ChatRoomServiceImpl chatService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

//    @Test
//    void createChatRoom() {
//        User user = new User();
//        user.setUserId(1L);
//        Plan plan = new Plan();
//        plan.setPlanId(1L);
//        ChatRoom chatRoom = new ChatRoom();
//        chatRoom.setRoomId(1L);
//        chatRoom.setRoomName("Test Room");
//        chatRoom.setChatUser(user);
//        chatRoom.setPlan(plan);
//
//        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
//        when(planRepository.findById(anyLong())).thenReturn(Optional.of(plan));
//        when(chatRoomRepository.save(any(ChatRoom.class))).thenReturn(chatRoom);
//
//        ChatRoomResponseDto responseDto = chatService.createChatRoom(1L, 1L, "Test Room");
//
//        assertNotNull(responseDto);
//        assertEquals(chatRoom.getRoomId(), responseDto.getRoomId());
//        assertEquals(chatRoom.getRoomName(), responseDto.getRoomName());
//    }
//
//    @Test
//    void updateChatRoomName() {
//        ChatRoom chatRoom = new ChatRoom();
//        chatRoom.setRoomId(1L);
//        chatRoom.setRoomName("Old Name");
//
//        when(chatRoomRepository.findById(anyLong())).thenReturn(Optional.of(chatRoom));
//        when(chatRoomRepository.save(any(ChatRoom.class))).thenReturn(chatRoom);
//
//        chatService.updateChatRoomName(1L, "New Name");
//
//        verify(chatRoomRepository, times(1)).findById(anyLong());
//        verify(chatRoomRepository, times(1)).save(any(ChatRoom.class));
//
//        assertEquals("New Name", chatRoom.getRoomName());
//    }
//
//    @Test
//    void addUserToChatRoom() {
//        ChatRoom chatRoom = new ChatRoom();
//        chatRoom.setRoomId(1L);
//        User user = new User();
//        user.setUserId(1L);
//
//        when(chatRoomRepository.findById(anyLong())).thenReturn(Optional.of(chatRoom));
//        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
//        when(chatRoomRepository.save(any(ChatRoom.class))).thenReturn(chatRoom);
//
//        chatService.addUserToChatRoom(1L, 1L);
//
//        verify(chatRoomRepository, times(1)).findById(anyLong());
//        verify(userRepository, times(1)).findById(anyLong());
//        verify(chatRoomRepository, times(1)).save(any(ChatRoom.class));
//
//        assertEquals(user, chatRoom.getChatUser());
//    }
//
//    @Test
//    void removeUserFromChatRoom() {
//        ChatRoom chatRoom = new ChatRoom();
//        chatRoom.setRoomId(1L);
//        User user = new User();
//        user.setUserId(1L);
//
//        when(chatRoomRepository.findById(anyLong())).thenReturn(Optional.of(chatRoom));
//        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
//        when(chatRoomRepository.save(any(ChatRoom.class))).thenReturn(chatRoom);
//
//        chatService.removeUserFromChatRoom(1L, 1L);
//
//        verify(chatRoomRepository, times(1)).findById(anyLong());
//        verify(userRepository, times(1)).findById(anyLong());
//        verify(chatRoomRepository, times(1)).save(any(ChatRoom.class));
//
//        assertNull(chatRoom.getChatUser());
//    }
}
