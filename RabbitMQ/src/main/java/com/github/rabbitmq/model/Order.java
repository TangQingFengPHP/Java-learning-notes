package com.github.rabbitmq.model;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class Order {
    private Long id;
    private String orderNo;
    private Long userId;
    private BigDecimal amount;
    private String status;
    private LocalDateTime createdAt;
}
