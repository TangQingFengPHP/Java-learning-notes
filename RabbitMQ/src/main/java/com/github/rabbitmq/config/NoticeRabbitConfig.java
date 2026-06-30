package com.github.rabbitmq.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.ExchangeBuilder;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class NoticeRabbitConfig {

    public static final String USER_REGISTERED_EXCHANGE = "user.registered.exchange";
    public static final String SMS_NOTICE_QUEUE = "sms.notice.queue";
    public static final String EMAIL_NOTICE_QUEUE = "email.notice.queue";

    @Bean
    public FanoutExchange userRegisteredExchange() {
        return ExchangeBuilder.fanoutExchange(USER_REGISTERED_EXCHANGE).durable(true).build();
    }

    @Bean
    public Queue smsNoticeQueue() {
        return QueueBuilder.durable(SMS_NOTICE_QUEUE).quorum().build();
    }

    @Bean
    public Queue emailNoticeQueue() {
        return QueueBuilder.durable(EMAIL_NOTICE_QUEUE).quorum().build();
    }

    @Bean
    public Binding smsNoticeBinding(Queue smsNoticeQueue, FanoutExchange userRegisteredExchange) {
        return BindingBuilder.bind(smsNoticeQueue).to(userRegisteredExchange);
    }

    @Bean
    public Binding emailNoticeBinding(Queue emailNoticeQueue, FanoutExchange userRegisteredExchange) {
        return BindingBuilder.bind(emailNoticeQueue).to(userRegisteredExchange);
    }
}
