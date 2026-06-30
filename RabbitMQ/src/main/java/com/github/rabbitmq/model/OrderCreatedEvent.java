package com.github.rabbitmq.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record OrderCreatedEvent(
        Long orderId,
        String orderNo,
        Long userId,
        BigDecimal amount,
        LocalDateTime createdAt
) {
}
