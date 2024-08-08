package org.example.todotravel.domain.chat.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.todotravel.domain.chat.service.ChatRoomService;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;

@WebMvcTest(ChatRoomController.class)
class ChatRoomControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ChatRoomService chatRoomService;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        Mockito.reset(chatRoomService);
    }

//    @Test
//    void testCreateChatRoom() throws Exception {
//        ChatRoomResponseDto responseDto = new ChatRoomResponseDto(1L, "Test Room");
//
//        Mockito.when(chatRoomService.createChatRoom(anyLong(), anyLong(), any(String.class)))
//                .thenReturn(responseDto);
//
//        ChatRoomCreateRequestDto requestDto = new ChatRoomCreateRequestDto(1L, 1L, "Test Room");
//
//        mockMvc.perform(post("/api/chat/create")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(requestDto)))
//                .andExpect(status().isOk())
//                .andExpect(content().json(objectMapper.writeValueAsString(new ApiResponse<>(true, "채팅방 생성 성공", responseDto))));
//    }
//
//    @Test
//    void testUpdateChatRoomName() throws Exception {
//        ChatRoomUpdateRequestDto requestDto = new ChatRoomUpdateRequestDto(1L, "New Room Name");
//
//        mockMvc.perform(put("/api/chat/update-name")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(requestDto)))
//                .andExpect(status().isOk())
//                .andExpect(content().json(objectMapper.writeValueAsString(new ApiResponse<>(true, "채팅방 이름 수정 성공"))));
//    }
//
//    @Test
//    void testJoinChatRoom() throws Exception {
//        mockMvc.perform(post("/api/chat/join/1")
//                        .param("userId", "1"))
//                .andExpect(status().isOk())
//                .andExpect(content().json(objectMapper.writeValueAsString(new ApiResponse<>(true, "채팅방 참여 성공"))));
//    }
//
//    @Test
//    void testLeaveChatRoom() throws Exception {
//        mockMvc.perform(post("/api/chat/leave/1")
//                        .param("userId", "1"))
//                .andExpect(status().isOk())
//                .andExpect(content().json(objectMapper.writeValueAsString(new ApiResponse<>(true, "채팅방 나가기 성공"))));
//    }
}
