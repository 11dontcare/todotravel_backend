package org.example.todotravel.domain.chat.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.todotravel.domain.chat.entity.ChatMessage;

import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChatMessageResponseDto {
    private Long userId;
    private Long roomId;
    private String nickname;
    private String content;
    private LocalDateTime createdAt;

    public static ChatMessageResponseDto of(ChatMessage chatMessage) {
        return new ChatMessageResponseDto(chatMessage.getUserId(), chatMessage.getRoomId(), chatMessage.getNickname(),
            chatMessage.getContent(), chatMessage.getCreateAt());
    }
}
