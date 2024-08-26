package org.example.todotravel.domain.chat.service;

import org.example.todotravel.domain.chat.dto.request.ChatMessageRequestDto;
import org.example.todotravel.domain.chat.dto.response.ChatMessageResponseDto;
import org.example.todotravel.domain.chat.entity.ChatMessage;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Set;

public interface ChatMessageService {
    Flux<ChatMessageResponseDto> findChatMessages(Long roomId);
    Mono<ChatMessage> saveChatMessage(ChatMessageRequestDto dto);
    Mono<Void> deleteAllMessageForChatRoom(Long roomId);
    Mono<Void> removeDeletedMessagesForRooms(Set<Long> roomIds);
    Mono<Void> updateNicknameForUser(Long userId, String newNickname);
    Mono<Void> restoreMessagesForChatRoom(Long roomId);
    Mono<Void> restoreNicknameForUser(Long userId, String oldNickname);
}
