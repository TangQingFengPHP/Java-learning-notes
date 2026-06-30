package com.github.rabbitmq.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.ExchangeBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BusinessEventRabbitConfig {

    public static final String BUSINESS_EVENT_EXCHANGE = "business.event.exchange";
    public static final String ORDER_EVENT_QUEUE = "order.event.queue";
    public static final String ALL_EVENT_QUEUE = "all.event.queue";

    @Bean
    public TopicExchange businessEventExchange() {
        return ExchangeBuilder.topicExchange(BUSINESS_EVENT_EXCHANGE).durable(true).build();
    }

    @Bean
    public Queue orderEventQueue() {
        return QueueBuilder.durable(ORDER_EVENT_QUEUE).quorum().build();
    }

    @Bean
    public Queue allEventQueue() {
        return QueueBuilder.durable(ALL_EVENT_QUEUE).quorum().build();
    }

    @Bean
    public Binding orderEventBinding(Queue orderEventQueue, TopicExchange businessEventExchange) {
        return BindingBuilder.bind(orderEventQueue).to(businessEventExchange).with("order.#");
    }

    @Bean
    public Binding allEventBinding(Queue allEventQueue, TopicExchange businessEventExchange) {
        return BindingBuilder.bind(allEventQueue).to(businessEventExchange).with("#");
    }
}
