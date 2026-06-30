package com.github.rabbitmq.model;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class MessageLog {
    private Long id;
    private String messageId;
    private String consumerName;
    private String routingKey;
    private String payloadSummary;
    private LocalDateTime createdAt;
}
