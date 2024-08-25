package org.example.todotravel.domain.chat.service.impl;

import java.util.Optional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.todotravel.domain.chat.dto.request.ChatMessageRequestDto;
import org.example.todotravel.domain.chat.dto.response.ChatMessageResponseDto;
import org.example.todotravel.domain.chat.entity.ChatMessage;
import org.example.todotravel.domain.chat.entity.ChatRoomUser;
import org.example.todotravel.domain.chat.repository.ChatMessageRepository;
import org.example.todotravel.domain.chat.repository.ChatRoomUserRepository;
import org.example.todotravel.domain.chat.service.ChatMessageService;
import org.example.todotravel.domain.user.entity.User;
import org.example.todotravel.domain.user.repository.UserRepository;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.reactive.TransactionalOperator;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChatMessageServiceImpl implements ChatMessageService {

    private final ChatMessageRepository chatMessageRepository;
    private final ChatRoomUserRepository chatRoomUserRepository;
    private final UserRepository userRepository;
    private final ReactiveMongoTemplate reactiveMongoTemplate;
    private final TransactionalOperator transactionalOperator;

    // 이전 채팅 내용 조회
    @Transactional
    public Flux<ChatMessageResponseDto> findChatMessages(Long roomId) {
        Flux<ChatMessage> chatMessages = chatMessageRepository.findAllByRoomId(roomId);
        return chatMessages.map(ChatMessageResponseDto::of);
    }

    // 메시지 저장
    @Transactional
    public Mono<ChatMessage> saveChatMessage(ChatMessageRequestDto dto) {
        Long userId = dto.getUserId();
        Long roomId = dto.getRoomId();

        User user = userRepository.findById(userId)
            .orElseThrow(() -> new IllegalArgumentException("User not found with id: " + userId));

        Optional<ChatRoomUser> chatRoomUserOpt = chatRoomUserRepository.findByUserAndChatRoomRoomId(user, roomId);

        return Mono.justOrEmpty(chatRoomUserOpt)
            .flatMap(chatRoomUser -> {
                ChatMessage chatMessage = new ChatMessage(
                    chatRoomUser.getUser().getUserId(),
                    chatRoomUser.getChatRoom().getRoomId(),
                    chatRoomUser.getUser().getNickname(),
                    dto.getContent()
                );
                return chatMessageRepository.save(chatMessage);
            });
    }

    // 회원 탈퇴 시 사용자가 생성한 채팅방의 모든 메시지 삭제
    @Override
    public Mono<Void> deleteAllMessageForChatRoom(Long roomId) {
        return transactionalOperator.execute(tx ->
                chatMessageRepository.findAllByRoomId(roomId)
                    .collectList()
                    .flatMap(messages -> {
                        if (!messages.isEmpty()) {
                            // 메시지가 있으면 삭제된 메시지 컬렉션에 저장 후 원본 삭제
                            return reactiveMongoTemplate.insert(messages, "deleted_messages")
                                .then(chatMessageRepository.deleteAllByRoomId(roomId));
                        }
                        log.info("No messages found for room ID: {}", roomId);
                        return Mono.empty();
                    })
            ).then()
            .doOnSuccess(v -> log.info("Deleted all messages for room ID: {}", roomId))
            .onErrorResume(e -> {
                log.error("Error deleting messages for room ID: {}", roomId, e);
                return Mono.empty(); // 오류 발생 시에도 Mono.empty()를 반환하여 계속 진행
            });
    }

    // 회원 탈퇴 과정에서 오류 발생 시 삭제한 메시지를 복구하는 대한 보상 트랜잭션
    @Override
    public Mono<Void> restoreMessagesForChatRoom(Long roomId) {
        return transactionalOperator.execute(tx ->
                reactiveMongoTemplate.find(
                        org.springframework.data.mongodb.core.query.Query.query(
                            org.springframework.data.mongodb.core.query.Criteria.where("roomId").is(roomId)
                        ),
                        ChatMessage.class,
                        "deleted_messages"
                    )
                    .collectList()
                    .flatMap(deletedMessages -> {
                        if (!deletedMessages.isEmpty()) {
                            // 삭제된 메시지가 있으면 원본 컬렉션에 복구 후 삭제된 메시지 컬렉션에서 제거
                            return chatMessageRepository.saveAll(deletedMessages)
                                .then(reactiveMongoTemplate.remove(
                                    org.springframework.data.mongodb.core.query.Query.query(
                                        org.springframework.data.mongodb.core.query.Criteria.where("roomId").is(roomId)
                                    ),
                                    "deleted_messages"
                                ));
                        }
                        return Mono.empty();
                    })
            ).then()
            .doOnSuccess(v -> log.info("Restored messages for room ID: {}", roomId))
            .onErrorResume(e -> {
                log.error("Error restoring messages for room ID: {}", roomId, e);
                return Mono.error(e);
            });
    }

    // 회원 탈퇴 시 채팅 내역에서 사용자 닉네임을 업데이트
    @Override
    public Mono<Void> updateNicknameForUser(Long userId, String newNickname) {
        return transactionalOperator.execute(tx ->
                chatMessageRepository.updateNicknameByUserId(userId, newNickname)
                    .flatMap(modifiedCount -> {
                        if (modifiedCount > 0) {
                            log.info("Updated nickname for user ID: {} to {}", userId, newNickname);
                        } else {
                            log.info("No messages found to update nickname for user ID: {}", userId);
                        }
                        return Mono.empty();
                    })
            ).then()
            .doOnSuccess(v -> log.info("Completed nickname update process for user ID: {}", userId))
            .onErrorResume(e -> {
                log.error("Error during nickname update process for user ID: {}", userId, e);
                return Mono.empty(); // 오류 발생 시에도 Mono.empty()를 반환하여 계속 진행
            });
    }

    // 회원 탈퇴 과정에서 오류 발생 시 이전 닉네임으로 복구하는 보상 트랜잭션 수행
    @Override
    public Mono<Void> restoreNicknameForUser(Long userId, String oldNickname) {
        return chatMessageRepository.updateNicknameByUserId(userId, oldNickname)
            .then()
            .doOnSuccess(v -> log.info("Restored old nickname for user ID: {}", userId))
            .onErrorResume(e -> {
                log.error("Error restoring old nickname for user ID: {}", userId, e);
                return Mono.empty();
            });
    }
}
