package com.github.rabbitmq.consumer;

import com.github.rabbitmq.config.BusinessEventRabbitConfig;
import com.github.rabbitmq.repository.MessageLogRepository;
import com.rabbitmq.client.Channel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class BusinessEventConsumer {

    private static final String ORDER_EVENT_CONSUMER = "order-event-consumer";
    private static final String ALL_EVENT_CONSUMER = "all-event-consumer";

    private final MessageLogRepository messageLogRepository;

    @RabbitListener(queues = BusinessEventRabbitConfig.ORDER_EVENT_QUEUE)
    public void handleOrderEvent(
            @Payload Map<String, Object> payload,
            Channel channel,
            @Header(AmqpHeaders.DELIVERY_TAG) long deliveryTag,
            @Header(AmqpHeaders.RECEIVED_ROUTING_KEY) String routingKey,
            @Header(value = AmqpHeaders.MESSAGE_ID, required = false) String messageId
    ) throws IOException {
        handle(payload, channel, deliveryTag, routingKey, messageId, ORDER_EVENT_CONSUMER);
    }

    @RabbitListener(queues = BusinessEventRabbitConfig.ALL_EVENT_QUEUE)
    public void handleAllEvent(
            @Payload Map<String, Object> payload,
            Channel channel,
            @Header(AmqpHeaders.DELIVERY_TAG) long deliveryTag,
            @Header(AmqpHeaders.RECEIVED_ROUTING_KEY) String routingKey,
            @Header(value = AmqpHeaders.MESSAGE_ID, required = false) String messageId
    ) throws IOException {
        handle(payload, channel, deliveryTag, routingKey, messageId, ALL_EVENT_CONSUMER);
    }

    private void handle(
            Map<String, Object> payload,
            Channel channel,
            long deliveryTag,
            String routingKey,
            String messageId,
            String consumerName
    ) throws IOException {
        try {
            String id = messageId == null ? routingKey + "-" + payload.hashCode() : messageId;
            if (messageLogRepository.exists(id, consumerName)) {
                channel.basicAck(deliveryTag, false);
                return;
            }

            log.info("Topic 事件 consumer={} routingKey={} payload={}", consumerName, routingKey, payload);

            messageLogRepository.save(id, consumerName, routingKey, payload.toString());
            channel.basicAck(deliveryTag, false);
        } catch (Exception e) {
            log.error("Topic 事件处理失败 consumer={}", consumerName, e);
            channel.basicNack(deliveryTag, false, false);
        }
    }
}
