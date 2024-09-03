package org.example.todotravel.global.exception;

import lombok.Getter;

@Getter
public class CustomJwtException extends RuntimeException {
    private final JwtExceptionCode jwtExceptionCode;

    public CustomJwtException(JwtExceptionCode jwtExceptionCode) {
        super(jwtExceptionCode.getMessage());
        this.jwtExceptionCode = jwtExceptionCode;
    }
}
