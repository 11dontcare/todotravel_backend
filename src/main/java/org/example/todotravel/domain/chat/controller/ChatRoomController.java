package org.example.todotravel.domain.chat.controller;

import lombok.RequiredArgsConstructor;
import org.example.todotravel.domain.chat.dto.request.ChatRoomCreateRequestDto;
import org.example.todotravel.domain.chat.dto.request.ChatRoomNameRequestDto;
import org.example.todotravel.domain.chat.dto.request.FirstUserCheckRequestDto;
import org.example.todotravel.domain.chat.dto.response.ChatRoomListResponseDto;
import org.example.todotravel.domain.chat.dto.response.ChatRoomNameResponseDto;
import org.example.todotravel.domain.chat.dto.response.ChatRoomResponseDto;
import org.example.todotravel.domain.chat.dto.response.ChatRoomUserResponseDto;
import org.example.todotravel.domain.chat.service.ChatRoomService;
import org.example.todotravel.domain.chat.service.ChatRoomUserService;
import org.example.todotravel.global.controller.ApiResponse;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/chat/rooms")
@RequiredArgsConstructor
public class ChatRoomController {
    private final ChatRoomService chatRoomService;
    private final ChatRoomUserService chatRoomUserService;

    // 플랜 생성 시 채팅방 생성
    @PostMapping
    public ApiResponse<ChatRoomResponseDto> createChatRoom(@RequestBody ChatRoomCreateRequestDto dto) {
        ChatRoomResponseDto chatRoomResponseDto = chatRoomService.createChatRoom(dto);
        return new ApiResponse<>(true, "채팅방 생성 성공", chatRoomResponseDto);
    }

    // 유저가 가진 채팅방 리스트 조회
    @GetMapping("/list/{userId}")
    public ApiResponse<?> getChatRooms(@PathVariable("userId") Long userId) {
        List<ChatRoomListResponseDto> chatRoomList = chatRoomUserService.getChatRoomsByUserId(userId);
        return new ApiResponse<>(true, "채팅방 리스트 조회 성공", chatRoomList);
    }

    // 채팅방 유저 목록 조회
    @GetMapping("/{roomId}/users")
    public ApiResponse<?> getUserList(@PathVariable("roomId") Long roomId) {
        List<ChatRoomUserResponseDto> chatRoomUserList = chatRoomUserService.getUsersByRoomId(roomId);
        return new ApiResponse<>(true, "채팅방 유저 목록 조회 성공", chatRoomUserList);
    }

    // 채팅방의 첫 번째 유저인지 판별
    @GetMapping("/first-user")
    public ApiResponse<?> isFirstUser(@RequestBody FirstUserCheckRequestDto dto) {
        boolean isFirstUser = chatRoomUserService.checkFirstUser(dto);
        return new ApiResponse<>(isFirstUser, "");
    }

    // 채팅방 이름 수정
    @PutMapping("/name")
    public ApiResponse<?> updateChatRoomName(@RequestBody ChatRoomNameRequestDto dto) {
        ChatRoomNameResponseDto updatedChatRoom = chatRoomService.updateChatRoomName(dto);
        return new ApiResponse<>(true, "채팅방 이름 수정 성공", updatedChatRoom);
    }

    // 채팅방 참여하기
    @PostMapping("/join/{roomId}")
    public ApiResponse<Void> joinChatRoom(@PathVariable Long roomId, @RequestParam Long userId) {
        chatRoomService.addUserToChatRoom(roomId, userId);
        return new ApiResponse<>(true, "채팅방 참여 성공");
    }

    // 채팅방에서 나가기 or 추방시키기
    @DeleteMapping("/leave/{roomId}")
    public ApiResponse<Void> leaveChatRoom(@PathVariable Long roomId, @RequestParam Long userId) {
        chatRoomService.removeUserFromChatRoom(roomId, userId);
        return new ApiResponse<>(true, "채팅방 나가기 성공");
    }

    // 채팅방 삭제
    @DeleteMapping("/{roomId}")
    public ApiResponse<?> deleteChatRoom(@PathVariable("roomId") Long roomId) {
        chatRoomService.deleteChatRoom(roomId);
        return new ApiResponse<>(true, "채팅방 삭제 성공");
    }
}
