package org.example.todotravel.domain.chat.entity;

import jakarta.persistence.Id;
import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDateTime;

@Document(collection = "deleted_messages")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DeletedMessage {
    @Id
    private String id;

    @Field("user_id")
    private Long userId;

    @Field("room_id")
    private Long roomId;

    @Field("nickname")
    private String nickname;

    @Field("content")
    private String content;

    @Field("created_at")
    private LocalDateTime createdAt;

    public static DeletedMessage fromChatMessage(ChatMessage chatMessage) {
        return DeletedMessage.builder()
            .userId(chatMessage.getUserId())
            .roomId(chatMessage.getRoomId())
            .nickname(chatMessage.getNickname())
            .content(chatMessage.getContent())
            .createdAt(chatMessage.getCreateAt())
            .build();
    }
}
