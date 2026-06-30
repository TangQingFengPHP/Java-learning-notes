package com.github.rabbitmq.service;

import com.github.rabbitmq.model.Order;
import com.github.rabbitmq.model.OrderCreateRequest;
import com.github.rabbitmq.model.OrderCreatedEvent;
import com.github.rabbitmq.model.OrderPaidEvent;
import com.github.rabbitmq.model.UserRegisterRequest;
import com.github.rabbitmq.model.UserRegisteredEvent;
import com.github.rabbitmq.producer.MessageProducer;
import com.github.rabbitmq.repository.OrderRepository;
import com.github.rabbitmq.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

@Service
@RequiredArgsConstructor
public class OrderService {

    private static final AtomicLong ORDER_SEQ = new AtomicLong(1);

    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final MessageProducer messageProducer;

    @Transactional
    public Map<String, Object> createOrder(OrderCreateRequest req) {
        String orderNo = "O" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"))
                + String.format("%04d", ORDER_SEQ.getAndIncrement());
        LocalDateTime now = LocalDateTime.now();

        Long orderId = orderRepository.save(orderNo, req.getUserId(), req.getAmount(), "CREATED", now);

        OrderCreatedEvent event = new OrderCreatedEvent(orderId, orderNo, req.getUserId(), req.getAmount(), now);
        String messageId = messageProducer.sendOrderCreated(event);

        Map<String, Object> result = new HashMap<>();
        result.put("orderId", orderId);
        result.put("orderNo", orderNo);
        result.put("messageId", messageId);
        result.put("hint", "Direct Exchange: order.exchange + routingKey=order.created");
        return result;
    }

    public Optional<Order> findById(Long id) {
        return orderRepository.findById(id);
    }

    @Transactional
    public Map<String, Object> payOrder(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("订单不存在"));
        if ("PAID".equals(order.getStatus())) {
            throw new IllegalArgumentException("订单已支付");
        }
        orderRepository.updateStatus(orderId, "PAID");

        OrderPaidEvent event = new OrderPaidEvent(
                order.getId(),
                order.getOrderNo(),
                order.getAmount(),
                LocalDateTime.now()
        );
        String messageId = messageProducer.sendOrderPaidTopic(event);

        Map<String, Object> result = new HashMap<>();
        result.put("orderId", orderId);
        result.put("messageId", messageId);
        result.put("hint", "Topic Exchange: business.event.exchange + routingKey=order.paid");
        return result;
    }

    @Transactional
    public Map<String, Object> registerUser(UserRegisterRequest req) {
        Long userId = userRepository.save(req.getUsername(), req.getEmail(), req.getPhone(), LocalDateTime.now());
        UserRegisteredEvent event = new UserRegisteredEvent(userId, req.getEmail(), req.getPhone());
        String messageId = messageProducer.sendUserRegistered(event);

        Map<String, Object> result = new HashMap<>();
        result.put("userId", userId);
        result.put("messageId", messageId);
        result.put("hint", "Fanout Exchange: user.registered.exchange 广播到 sms/email 队列");
        return result;
    }
}
