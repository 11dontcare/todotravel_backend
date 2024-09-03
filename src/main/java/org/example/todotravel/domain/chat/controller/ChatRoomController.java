package org.example.todotravel.domain.chat.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.todotravel.domain.chat.dto.request.ChatRoomNameRequestDto;
import org.example.todotravel.domain.chat.dto.request.FirstUserCheckRequestDto;
import org.example.todotravel.domain.chat.dto.request.OneToOneChatRoomRequestDto;
import org.example.todotravel.domain.chat.dto.response.ChatMessageResponseDto;
import org.example.todotravel.domain.chat.dto.response.ChatRoomListResponseDto;
import org.example.todotravel.domain.chat.dto.response.ChatRoomNameResponseDto;
import org.example.todotravel.domain.chat.dto.response.ChatRoomResponseDto;
import org.example.todotravel.domain.chat.dto.response.ChatRoomUserResponseDto;
import org.example.todotravel.domain.chat.service.ChatMessageService;
import org.example.todotravel.domain.chat.service.ChatRoomService;
import org.example.todotravel.domain.chat.service.ChatRoomUserService;
import org.example.todotravel.domain.plan.entity.Plan;
import org.example.todotravel.domain.plan.service.PlanService;
import org.example.todotravel.domain.plan.service.PlanUserService;
import org.example.todotravel.global.controller.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/chat/rooms")
@RequiredArgsConstructor
public class ChatRoomController {

    private final ChatRoomService chatRoomService;
    private final ChatRoomUserService chatRoomUserService;
    private final ChatMessageService chatMessageService;
    private final PlanUserService planUserService;
    private final PlanService planService;

    // 1:1 채팅방 생성
    @PostMapping("/one-to-one")
    public ApiResponse<?> createOneToOneChatRoom(@Valid @RequestBody OneToOneChatRoomRequestDto dto) {
        ChatRoomResponseDto chatRoomResponseDto = chatRoomService.createOneToOneChatRoom(dto);
        return new ApiResponse<>(true, "1:1 채팅방 생성 성공", chatRoomResponseDto);
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

    // 채팅방에서 나가기 or 추방시키기
    @DeleteMapping("/leave/{roomId}")
    public ApiResponse<Void> leaveChatRoom(@PathVariable("roomId") Long roomId,
                                           @RequestParam("userId") Long userId) {
        // 채팅방에서 해당 유저 제거
        chatRoomService.removeUserFromChatRoom(roomId, userId);

        // 플랜에서도 해당 유저 제거 - 1:1인 경우는 플랜이 없음
        Plan plan = chatRoomService.getPlanByRoomId(roomId);
        if (plan != null) {
            planUserService.removePlanUser(plan.getPlanId(), userId);
        }

        return new ApiResponse<>(true, "채팅방 나가기 성공");
    }

    // 채팅방 삭제
    @DeleteMapping("/{roomId}")
    public ApiResponse<?> deleteChatRoom(@PathVariable("roomId") Long roomId) {
        // 채팅방 삭제 시 플랜도 삭제 - 1:1인 경우는 플랜이 없음
        Plan plan = chatRoomService.getPlanByRoomId(roomId);
        //외래키 제약조건으로 인해 채팅방 먼저 삭제
        chatRoomService.removeChatRoomAndMessage(roomId);
        if (plan != null) {
            planUserService.removePlanUserFromOwnPlan(plan);
            planService.removePlan(plan);
        }
        return new ApiResponse<>(true, "채팅방 삭제 성공");
    }

    // 이전 채팅 내용 조회
    @GetMapping("/find/comment-list/{roomId}")
    public Mono<ApiResponse<List<ChatMessageResponseDto>>> find(@PathVariable("roomId") Long roomId) {
        Flux<ChatMessageResponseDto> response = chatMessageService.getChatMessages(roomId);
        return response.collectList()
            .map(messages -> new ApiResponse<>(true, "이전 채팅 내역 조회 성공", messages));
    }
}
