package com.github.rabbitmq.model;

public record UserRegisteredEvent(
        Long userId,
        String email,
        String phone
) {
}
