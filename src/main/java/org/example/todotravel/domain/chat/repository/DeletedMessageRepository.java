package org.example.todotravel.domain.chat.repository;

import org.example.todotravel.domain.chat.entity.DeletedMessage;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface DeletedMessageRepository extends ReactiveMongoRepository<DeletedMessage, String> {
    Flux<DeletedMessage> findAllByRoomId(Long roomId);
    Mono<Void> deleteAllByRoomId(Long roomId);
}
