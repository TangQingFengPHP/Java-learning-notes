package com.github.rabbitmq.producer;

import com.github.rabbitmq.config.BusinessEventRabbitConfig;
import com.github.rabbitmq.config.DeadLetterRabbitConfig;
import com.github.rabbitmq.config.NoticeRabbitConfig;
import com.github.rabbitmq.config.OrderRabbitConfig;
import com.github.rabbitmq.model.OrderCreatedEvent;
import com.github.rabbitmq.model.OrderPaidEvent;
import com.github.rabbitmq.model.ReliableMessage;
import com.github.rabbitmq.model.UserRegisteredEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MessageProducer {

    private final RabbitTemplate rabbitTemplate;

    public String sendOrderCreated(OrderCreatedEvent event) {
        String messageId = UUID.randomUUID().toString();
        ReliableMessage<OrderCreatedEvent> message = new ReliableMessage<>(messageId, event);
        rabbitTemplate.convertAndSend(
                OrderRabbitConfig.ORDER_EXCHANGE,
                OrderRabbitConfig.ORDER_CREATED_ROUTING_KEY,
                message,
                new CorrelationData(messageId)
        );
        return messageId;
    }

    public String sendUserRegistered(UserRegisteredEvent event) {
        String messageId = UUID.randomUUID().toString();
        rabbitTemplate.convertAndSend(
                NoticeRabbitConfig.USER_REGISTERED_EXCHANGE,
                "",
                event,
                new CorrelationData(messageId)
        );
        return messageId;
    }

    public String sendBusinessEvent(String routingKey, Object payload) {
        String messageId = UUID.randomUUID().toString();
        rabbitTemplate.convertAndSend(
                BusinessEventRabbitConfig.BUSINESS_EVENT_EXCHANGE,
                routingKey,
                payload,
                new CorrelationData(messageId)
        );
        return messageId;
    }

    public String sendOrderPaidTopic(OrderPaidEvent event) {
        return sendBusinessEvent("order.paid", event);
    }

    public String sendDlxDemo(OrderCreatedEvent event) {
        String messageId = UUID.randomUUID().toString();
        rabbitTemplate.convertAndSend(
                DeadLetterRabbitConfig.ORDER_DLX_DEMO_EXCHANGE,
                DeadLetterRabbitConfig.ORDER_DLX_DEMO_ROUTING_KEY,
                event,
                new CorrelationData(messageId)
        );
        return messageId;
    }

    public String sendInvalidRouteDemo() {
        String messageId = UUID.randomUUID().toString();
        rabbitTemplate.convertAndSend(
                OrderRabbitConfig.ORDER_EXCHANGE,
                "invalid.routing.key",
                Map.of("demo", "route-fail"),
                new CorrelationData(messageId)
        );
        return messageId;
    }
}
