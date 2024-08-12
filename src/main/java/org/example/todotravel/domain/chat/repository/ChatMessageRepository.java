package org.example.todotravel.domain.chat.repository;

import org.example.todotravel.domain.chat.entity.ChatMessage;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;

public interface ChatMessageRepository extends ReactiveMongoRepository<ChatMessage, String> {
    Flux<ChatMessage> findAllByRoomId(Long roomId);
}
