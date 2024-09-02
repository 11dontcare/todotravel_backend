package org.example.todotravel.domain.chat.repository;

import org.example.todotravel.domain.chat.entity.DeletedMessage;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface DeletedMessageRepository extends ReactiveMongoRepository<DeletedMessage, String> {

    // roomId로 삭제하려던 모든 메시지 조회
    Flux<DeletedMessage> findAllByRoomId(Long roomId);

    // 해당 채팅방의 모든 메시지 삭제
    Mono<Void> deleteAllByRoomId(Long roomId);
}
