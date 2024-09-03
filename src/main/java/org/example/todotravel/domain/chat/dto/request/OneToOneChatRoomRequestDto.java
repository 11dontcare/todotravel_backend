package org.example.todotravel.domain.chat.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OneToOneChatRoomRequestDto {
    @NotNull
    private Long senderId;

    @NotNull
    private Long receiverId;
}
