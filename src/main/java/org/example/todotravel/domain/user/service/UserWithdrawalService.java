package org.example.todotravel.domain.user.service;

import org.example.todotravel.domain.user.entity.User;
import reactor.core.publisher.Mono;

public interface UserWithdrawalService {
    void withdrawUser(User user);
}
