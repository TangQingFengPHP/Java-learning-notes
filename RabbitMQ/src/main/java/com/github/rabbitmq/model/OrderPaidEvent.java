package com.github.rabbitmq.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record OrderPaidEvent(
        Long orderId,
        String orderNo,
        BigDecimal amount,
        LocalDateTime paidAt
) {
}
