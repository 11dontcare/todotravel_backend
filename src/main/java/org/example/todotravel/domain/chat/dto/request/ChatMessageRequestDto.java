package org.example.todotravel.domain.chat.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.todotravel.domain.chat.entity.ChatMessage;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChatMessageRequestDto {
    private Long userId;
    private Long roomId;
    private String nickname;
    private String content;

    public static ChatMessageRequestDto of(ChatMessage chatMessage) {
        return new ChatMessageRequestDto(chatMessage.getUserId(), chatMessage.getRoomId(), chatMessage.getNickname(),
            chatMessage.getContent());
    }
}
