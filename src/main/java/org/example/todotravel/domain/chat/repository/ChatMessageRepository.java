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

    // 채팅방에 해당하는 메시지 전부 불러오기
    Flux<ChatMessage> findAllByRoomId(Long roomId);

    // userId로 사용자 닉네임 업데이트
    @Query("{ 'userId':  ?0 }")
    @Update("{ '$set': { 'nickname': ?1 } }")
    Mono<Long> updateNicknameByUserId(Long userId, String newNickname);

    // 채팅방의 모든 메세지 제거
    Mono<Void> deleteAllByRoomId(Long roomId);
}
