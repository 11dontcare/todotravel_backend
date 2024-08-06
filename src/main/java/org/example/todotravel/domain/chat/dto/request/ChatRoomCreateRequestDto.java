package org.example.todotravel.domain.chat.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ChatRoomCreateRequestDto {
    private Long planId;
    private Long userId;
    private String roomName; // 이 필드를 추가했습니다.
}
