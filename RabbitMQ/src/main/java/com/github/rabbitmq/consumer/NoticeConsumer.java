package com.github.rabbitmq.consumer;

import com.github.rabbitmq.config.NoticeRabbitConfig;
import com.github.rabbitmq.model.UserRegisteredEvent;
import com.github.rabbitmq.repository.MessageLogRepository;
import com.rabbitmq.client.Channel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class NoticeConsumer {

    private static final String SMS_CONSUMER = "sms-notice-consumer";
    private static final String EMAIL_CONSUMER = "email-notice-consumer";

    private final MessageLogRepository messageLogRepository;

    @RabbitListener(queues = NoticeRabbitConfig.SMS_NOTICE_QUEUE)
    public void sendSms(
            UserRegisteredEvent event,
            Channel channel,
            @Header(AmqpHeaders.DELIVERY_TAG) long deliveryTag,
            @Header(value = AmqpHeaders.MESSAGE_ID, required = false) String messageId
    ) throws IOException {
        handleNotice(event, channel, deliveryTag, messageId, SMS_CONSUMER, "sms");
    }

    @RabbitListener(queues = NoticeRabbitConfig.EMAIL_NOTICE_QUEUE)
    public void sendEmail(
            UserRegisteredEvent event,
            Channel channel,
            @Header(AmqpHeaders.DELIVERY_TAG) long deliveryTag,
            @Header(value = AmqpHeaders.MESSAGE_ID, required = false) String messageId
    ) throws IOException {
        handleNotice(event, channel, deliveryTag, messageId, EMAIL_CONSUMER, "email");
    }

    private void handleNotice(
            UserRegisteredEvent event,
            Channel channel,
            long deliveryTag,
            String messageId,
            String consumerName,
            String channelType
    ) throws IOException {
        try {
            String id = messageId == null ? event.userId() + "-" + channelType : messageId;
            if (messageLogRepository.exists(id, consumerName)) {
                channel.basicAck(deliveryTag, false);
                return;
            }

            if ("sms".equals(channelType)) {
                log.info("发送短信 userId={} phone={}", event.userId(), event.phone());
            } else {
                log.info("发送邮件 userId={} email={}", event.userId(), event.email());
            }

            messageLogRepository.save(id, consumerName, NoticeRabbitConfig.USER_REGISTERED_EXCHANGE,
                    channelType + " userId=" + event.userId());
            channel.basicAck(deliveryTag, false);
        } catch (Exception e) {
            log.error("{} 通知处理失败", channelType, e);
            channel.basicNack(deliveryTag, false, true);
        }
    }
}
