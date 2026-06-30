package com.github.rabbitmq.consumer;

import com.github.rabbitmq.config.DeadLetterRabbitConfig;
import com.github.rabbitmq.config.NoticeRabbitConfig;
import com.github.rabbitmq.config.OrderRabbitConfig;
import com.github.rabbitmq.model.OrderCreatedEvent;
import com.github.rabbitmq.model.ReliableMessage;
import com.github.rabbitmq.model.UserRegisteredEvent;
import com.github.rabbitmq.repository.MessageLogRepository;
import com.github.rabbitmq.repository.OrderRepository;
import com.rabbitmq.client.Channel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class OrderCreatedConsumer {

    private static final String CONSUMER_NAME = "order-created-consumer";

    private final MessageLogRepository messageLogRepository;
    private final OrderRepository orderRepository;

    @Value("${app.order.fail-once:false}")
    private boolean failOnce;

    private volatile boolean failedOnce;

    @RabbitListener(queues = OrderRabbitConfig.ORDER_CREATED_QUEUE)
    public void handle(
            ReliableMessage<OrderCreatedEvent> message,
            Channel channel,
            @Header(AmqpHeaders.DELIVERY_TAG) long deliveryTag
    ) throws IOException {
        try {
            if (message == null || message.messageId() == null) {
                channel.basicNack(deliveryTag, false, false);
                return;
            }

            if (messageLogRepository.exists(message.messageId(), CONSUMER_NAME)) {
                log.info("幂等跳过 messageId={}", message.messageId());
                channel.basicAck(deliveryTag, false);
                return;
            }

            if (failOnce && !failedOnce) {
                failedOnce = true;
                throw new IllegalStateException("模拟首次消费失败");
            }

            OrderCreatedEvent event = message.payload();
            log.info("处理订单创建消息 messageId={} orderNo={}", message.messageId(), event.orderNo());

            orderRepository.updateStatus(event.orderId(), "PROCESSING");

            messageLogRepository.save(
                    message.messageId(),
                    CONSUMER_NAME,
                    OrderRabbitConfig.ORDER_CREATED_ROUTING_KEY,
                    "orderNo=" + event.orderNo()
            );

            channel.basicAck(deliveryTag, false);
        } catch (Exception e) {
            log.error("订单创建消息处理失败", e);
            channel.basicNack(deliveryTag, false, false);
        }
    }
}
