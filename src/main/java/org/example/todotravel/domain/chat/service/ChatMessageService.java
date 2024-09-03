package org.example.todotravel.domain.chat.service;

import org.example.todotravel.domain.chat.dto.request.ChatMessageRequestDto;
import org.example.todotravel.domain.chat.dto.response.ChatMessageResponseDto;
import org.example.todotravel.domain.chat.entity.ChatMessage;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Set;

public interface ChatMessageService {

    // 이전 채팅 내역 조회
    Flux<ChatMessageResponseDto> getChatMessages(Long roomId);

    // 메시지 저장
    Mono<ChatMessage> saveChatMessage(ChatMessageRequestDto dto);

    // 회원 탈퇴 시 사용자가 생성한 채팅방의 모든 메시지 삭제
    Mono<Void> removeAllMessageForChatRoom(Long roomId);

    // 회원 탈퇴 완료 후 deleted_messages 컬렉션에서 해당 채팅방의 메시지 제거
    Mono<Void> removeDeletedMessagesForRooms(Set<Long> roomIds);

    // 회원 탈퇴 과정에서 오류 발생 시 삭제한 메시지를 복구하는 대한 보상 트랜잭션
    Mono<Void> updateNicknameForUser(Long userId, String newNickname);

    // 회원 탈퇴 시 채팅 내역에서 사용자 닉네임을 업데이트
    Mono<Void> restoreMessagesForChatRoom(Long roomId);

    // 회원 탈퇴 과정에서 오류 발생 시 이전 닉네임으로 복구하는 보상 트랜잭션 수행
    Mono<Void> restoreNicknameForUser(Long userId, String oldNickname);
}
