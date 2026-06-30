package com.github.rabbitmq.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.ExchangeBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DeadLetterRabbitConfig {

    public static final String ORDER_DLX_DEMO_EXCHANGE = "order.dlx.demo.exchange";
    public static final String ORDER_DLX_DEMO_QUEUE = "order.dlx.demo.queue";
    public static final String ORDER_DLX_DEMO_ROUTING_KEY = "order.created";

    public static final String ORDER_DEAD_EXCHANGE = "order.dead.exchange";
    public static final String ORDER_DEAD_QUEUE = "order.dead.queue";
    public static final String ORDER_DEAD_ROUTING_KEY = "order.dead";

    @Bean
    public DirectExchange orderDlxDemoExchange() {
        return ExchangeBuilder.directExchange(ORDER_DLX_DEMO_EXCHANGE).durable(true).build();
    }

    @Bean
    public DirectExchange orderDeadExchange() {
        return ExchangeBuilder.directExchange(ORDER_DEAD_EXCHANGE).durable(true).build();
    }

    @Bean
    public Queue orderDlxDemoQueue() {
        return QueueBuilder
                .durable(ORDER_DLX_DEMO_QUEUE)
                .quorum()
                .deadLetterExchange(ORDER_DEAD_EXCHANGE)
                .deadLetterRoutingKey(ORDER_DEAD_ROUTING_KEY)
                .build();
    }

    @Bean
    public Queue orderDeadQueue() {
        return QueueBuilder.durable(ORDER_DEAD_QUEUE).quorum().build();
    }

    @Bean
    public Binding orderDlxDemoBinding(Queue orderDlxDemoQueue, DirectExchange orderDlxDemoExchange) {
        return BindingBuilder.bind(orderDlxDemoQueue).to(orderDlxDemoExchange).with(ORDER_DLX_DEMO_ROUTING_KEY);
    }

    @Bean
    public Binding orderDeadBinding(Queue orderDeadQueue, DirectExchange orderDeadExchange) {
        return BindingBuilder.bind(orderDeadQueue).to(orderDeadExchange).with(ORDER_DEAD_ROUTING_KEY);
    }
}
