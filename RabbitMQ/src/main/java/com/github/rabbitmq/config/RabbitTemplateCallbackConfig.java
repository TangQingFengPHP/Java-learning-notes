package com.github.rabbitmq.config;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.core.ReturnedMessage;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class RabbitTemplateCallbackConfig {

    private final RabbitTemplate rabbitTemplate;

    public RabbitTemplateCallbackConfig(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @PostConstruct
    public void init() {
        rabbitTemplate.setConfirmCallback(this::confirm);
        rabbitTemplate.setReturnsCallback(this::returned);
    }

    private void confirm(CorrelationData correlationData, boolean ack, String cause) {
        if (ack) {
            log.info("发布确认成功 correlationId={}", correlationData == null ? null : correlationData.getId());
        } else {
            log.warn("发布确认失败 correlationId={} cause={}",
                    correlationData == null ? null : correlationData.getId(), cause);
        }
    }

    private void returned(ReturnedMessage returnedMessage) {
        log.warn("消息路由失败 exchange={} routingKey={} replyText={}",
                returnedMessage.getExchange(),
                returnedMessage.getRoutingKey(),
                returnedMessage.getReplyText());
    }
}
