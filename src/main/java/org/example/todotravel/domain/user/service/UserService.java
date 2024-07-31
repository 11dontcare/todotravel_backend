package org.example.todotravel.domain.user.service;

import org.example.todotravel.domain.user.entity.User;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public interface UserService {
    public Optional<User> getUserByUserId(Long userId);
}
