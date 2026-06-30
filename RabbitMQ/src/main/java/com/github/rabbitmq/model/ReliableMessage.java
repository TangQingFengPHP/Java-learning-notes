package com.github.rabbitmq.model;

public record ReliableMessage<T>(
        String messageId,
        T payload
) {
}
