package org.example.todotravel.domain.user.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Role {

    ROLE_GUEST("ROLE_GUEST"), ROLE_USER("ROLE_USER");

    private final String key;
}
