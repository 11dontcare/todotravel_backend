package org.example.todotravel.domain.chat.service;

import org.example.todotravel.domain.chat.dto.request.ChatMessageRequestDto;
import org.example.todotravel.domain.chat.dto.response.ChatMessageResponseDto;
import org.example.todotravel.domain.chat.entity.ChatMessage;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ChatMessageService {
    Flux<ChatMessageResponseDto> findChatMessages(Long roomId);
    Mono<ChatMessage> saveChatMessage(ChatMessageRequestDto dto);
}
