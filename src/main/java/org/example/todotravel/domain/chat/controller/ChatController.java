package org.example.todotravel.domain.chat.controller;

import lombok.RequiredArgsConstructor;
import org.example.todotravel.domain.chat.dto.request.ChatRoomCreateRequestDto;
import org.example.todotravel.domain.chat.dto.request.ChatRoomUpdateRequestDto;
import org.example.todotravel.domain.chat.dto.response.ChatRoomResponseDto;
import org.example.todotravel.domain.chat.service.ChatService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/chat")
@RequiredArgsConstructor
public class ChatController {

    private final ChatService chatService;

    @PostMapping("/create")
    public ResponseEntity<ChatRoomResponseDto> createChatRoom(@RequestBody ChatRoomCreateRequestDto dto) {
        ChatRoomResponseDto chatRoomResponseDto = chatService.createChatRoom(dto.getUserId(), dto.getPlanId(), dto.getRoomName());
        return ResponseEntity.ok(chatRoomResponseDto);
    }

    @PutMapping("/update-name")
    public ResponseEntity<Void> updateChatRoomName(@RequestBody ChatRoomUpdateRequestDto dto) {
        chatService.updateChatRoomName(dto.getRoomId(), dto.getNewRoomName());
        return ResponseEntity.ok().build();
    }

    @PostMapping("/join/{roomId}")
    public ResponseEntity<Void> joinChatRoom(@PathVariable Long roomId, @RequestParam Long userId) {
        chatService.addUserToChatRoom(roomId, userId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/leave/{roomId}")
    public ResponseEntity<Void> leaveChatRoom(@PathVariable Long roomId, @RequestParam Long userId) {
        chatService.removeUserFromChatRoom(roomId, userId);
        return ResponseEntity.ok().build();
    }
}
