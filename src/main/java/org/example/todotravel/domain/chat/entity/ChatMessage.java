package org.example.todotravel.domain.chat.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.FieldType;

@Document(collection = "chatting_content")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChatMessage {
    @Id
    @Field(value = "message_id", targetType = FieldType.OBJECT_ID)
    private Long messageId;

    @Field("user_id")
    private Long userId;

    @Field("room_id")
    private Long roomId;

    @Field("nickname")
    private String nickname;

    @Field("content")
    private String content;

    @Field("created_at")
    private LocalDateTime createAt;

    public ChatMessage(Long userId, Long roomId, String nickname, String content) {
        this.userId = userId;
        this.roomId = roomId;
        this.nickname = nickname;
        this.content = content;
        this.createAt = LocalDateTime.now();
    }
}
