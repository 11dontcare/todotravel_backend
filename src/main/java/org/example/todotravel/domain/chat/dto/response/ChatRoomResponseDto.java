package org.example.todotravel.domain.chat.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.todotravel.domain.chat.entity.ChatRoom;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChatRoomResponseDto {
    private Long roomId;
    private String roomName;

    public ChatRoomResponseDto(ChatRoom savedChatRoom) {
        this.roomId = savedChatRoom.getRoomId();
        this.roomName = savedChatRoom.getRoomName();
    }
}
