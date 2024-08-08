package org.example.todotravel.domain.chat.controller;

import lombok.RequiredArgsConstructor;
import org.example.todotravel.domain.chat.dto.request.ChatRoomCreateRequestDto;
import org.example.todotravel.domain.chat.dto.request.ChatRoomUpdateRequestDto;
import org.example.todotravel.domain.chat.dto.response.ChatRoomResponseDto;
import org.example.todotravel.domain.chat.service.ChatService;
import org.example.todotravel.global.controller.ApiResponse;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/chat")
@RequiredArgsConstructor
public class ChatController {

    private final ChatService chatService;

    @PostMapping("/create")
    public ApiResponse<ChatRoomResponseDto> createChatRoom(@RequestBody ChatRoomCreateRequestDto dto) {
        ChatRoomResponseDto chatRoomResponseDto = chatService.createChatRoom(dto.getUserId(), dto.getPlanId(), dto.getRoomName());
        return new ApiResponse<>(true, "채팅방 생성 성공", chatRoomResponseDto);
    }

    @PutMapping("/update-name")
    public ApiResponse<Void> updateChatRoomName(@RequestBody ChatRoomUpdateRequestDto dto) {
        chatService.updateChatRoomName(dto.getRoomId(), dto.getNewRoomName());
        return new ApiResponse<>(true, "채팅방 이름 수정 성공");
    }

    @PostMapping("/join/{roomId}")
    public ApiResponse<Void> joinChatRoom(@PathVariable Long roomId, @RequestParam Long userId) {
        chatService.addUserToChatRoom(roomId, userId);
        return new ApiResponse<>(true, "채팅방 참여 성공");
    }

    @PostMapping("/leave/{roomId}")
    public ApiResponse<Void> leaveChatRoom(@PathVariable Long roomId, @RequestParam Long userId) {
        chatService.removeUserFromChatRoom(roomId, userId);
        return new ApiResponse<>(true, "채팅방 나가기 성공");
    }
}
