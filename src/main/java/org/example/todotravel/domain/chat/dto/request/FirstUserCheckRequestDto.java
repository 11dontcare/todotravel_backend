package org.example.todotravel.domain.chat.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class FirstUserCheckRequestDto {
    private Long roomId;
    private Long userId;
}
