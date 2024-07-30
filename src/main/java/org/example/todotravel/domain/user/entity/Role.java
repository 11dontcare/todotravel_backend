package org.example.todotravel.domain.user.entity;

import lombok.Getter;
@Getter
public enum Role {
    ROLE_USER("user"), ROLE_ADMIN("admin");
    String description;

    Role(String description) {
        this.description = description;
    }
}
