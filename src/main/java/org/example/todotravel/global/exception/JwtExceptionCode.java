package org.example.todotravel.global.exception;

import lombok.Getter;

/**
 * 토큰 예외 코드
 */
public enum JwtExceptionCode {
    NOT_FOUND_TOKEN("NOT_FOUND_TOKEN", "Headers에 토큰 형식의 값 찾을 수 없음"),
    INVALID_TOKEN("INVALID_TOKEN", "유효하지 않은 토큰"),
    EXPIRED_TOKEN("EXPIRED_TOKEN", "기간이 만료된 토큰"),
    UNSUPPORTED_TOKEN("UNSUPPORTED_TOKEN", "지원하지 않는 토큰"),
    UNKNOWN_ERROR("UNKNOWN_ERROR", "UNKNOWN_ERROR");

    @Getter
    private final String code;      // 예외 코드

    @Getter
    private final String message;   // 예외 메시지

    JwtExceptionCode(String code, String message) {
        this.code = code;
        this.message = message;
    }
}
