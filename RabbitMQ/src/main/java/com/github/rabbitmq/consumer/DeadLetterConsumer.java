package com.github.rabbitmq.consumer;

import com.github.rabbitmq.config.DeadLetterRabbitConfig;
import com.github.rabbitmq.model.OrderCreatedEvent;
import com.github.rabbitmq.repository.MessageLogRepository;
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
public class DeadLetterConsumer {

    private static final String DLX_DEMO_CONSUMER = "dlx-demo-consumer";
    private static final String DEAD_LETTER_CONSUMER = "dead-letter-consumer";

    private final MessageLogRepository messageLogRepository;

    @Value("${app.dlx.always-fail:false}")
    private boolean alwaysFail;

    @RabbitListener(queues = DeadLetterRabbitConfig.ORDER_DLX_DEMO_QUEUE)
    public void handleDlxDemo(
            OrderCreatedEvent event,
            Channel channel,
            @Header(AmqpHeaders.DELIVERY_TAG) long deliveryTag,
            @Header(value = AmqpHeaders.MESSAGE_ID, required = false) String messageId
    ) throws IOException {
        try {
            if (alwaysFail) {
                log.warn("DLX Demo 故意失败 orderNo={}", event.orderNo());
                channel.basicNack(deliveryTag, false, false);
                return;
            }

            String id = messageId == null ? event.orderNo() : messageId;
            log.info("DLX Demo 正常消费 orderNo={}", event.orderNo());
            messageLogRepository.save(id, DLX_DEMO_CONSUMER, DeadLetterRabbitConfig.ORDER_DLX_DEMO_ROUTING_KEY,
                    "orderNo=" + event.orderNo());
            channel.basicAck(deliveryTag, false);
        } catch (Exception e) {
            channel.basicNack(deliveryTag, false, false);
        }
    }

    @RabbitListener(queues = DeadLetterRabbitConfig.ORDER_DEAD_QUEUE)
    public void handleDeadLetter(
            OrderCreatedEvent event,
            Channel channel,
            @Header(AmqpHeaders.DELIVERY_TAG) long deliveryTag,
            @Header(value = AmqpHeaders.MESSAGE_ID, required = false) String messageId
    ) throws IOException {
        try {
            String id = messageId == null ? "dead-" + event.orderNo() : messageId;
            log.warn("收到死信消息 orderNo={} orderId={}", event.orderNo(), event.orderId());
            messageLogRepository.save(id, DEAD_LETTER_CONSUMER, DeadLetterRabbitConfig.ORDER_DEAD_ROUTING_KEY,
                    "dead orderNo=" + event.orderNo());
            channel.basicAck(deliveryTag, false);
        } catch (Exception e) {
            channel.basicNack(deliveryTag, false, false);
        }
    }
}
