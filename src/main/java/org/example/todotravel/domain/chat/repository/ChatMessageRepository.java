package org.example.todotravel.domain.chat.repository;

import org.example.todotravel.domain.chat.entity.ChatMessage;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.data.mongodb.repository.Update;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface ChatMessageRepository extends ReactiveMongoRepository<ChatMessage, String> {
    Flux<ChatMessage> findAllByRoomId(Long roomId);

    @Query("{ 'userId':  ?0 }")
    @Update("{ '$set': { 'nickname': ?1 } }")
    Mono<Long> updateNicknameByUserId(Long userId, String newNickname);

    Mono<ChatMessage> findFirstByUserId(Long userId);

    Mono<Void> deleteAllByRoomId(Long roomId);
}
