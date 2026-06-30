package com.github.rabbitmq.controller;

import com.github.rabbitmq.model.ApiError;
import com.github.rabbitmq.model.BusinessEventRequest;
import com.github.rabbitmq.model.Order;
import com.github.rabbitmq.model.OrderCreateRequest;
import com.github.rabbitmq.model.OrderCreatedEvent;
import com.github.rabbitmq.model.UserRegisterRequest;
import com.github.rabbitmq.producer.MessageProducer;
import com.github.rabbitmq.repository.MessageLogRepository;
import com.github.rabbitmq.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping
@RequiredArgsConstructor
public class DemoController {

    private final OrderService orderService;
    private final MessageProducer messageProducer;
    private final MessageLogRepository messageLogRepository;

    @PostMapping("/orders")
    public Map<String, Object> createOrder(@Valid @RequestBody OrderCreateRequest req) {
        return orderService.createOrder(req);
    }

    @GetMapping("/orders/{id}")
    public Order orderDetail(@PathVariable Long id) {
        return orderService.findById(id).orElseThrow(() -> new IllegalArgumentException("订单不存在"));
    }

    @PostMapping("/orders/{id}/pay")
    public Map<String, Object> payOrder(@PathVariable Long id) {
        return orderService.payOrder(id);
    }

    @PostMapping("/users/register")
    public Map<String, Object> register(@Valid @RequestBody UserRegisterRequest req) {
        return orderService.registerUser(req);
    }

    @PostMapping("/events/business")
    public Map<String, Object> sendBusinessEvent(@Valid @RequestBody BusinessEventRequest req) {
        Map<String, Object> payload = Map.of(
                "content", req.getContent(),
                "sentAt", LocalDateTime.now().toString()
        );
        String messageId = messageProducer.sendBusinessEvent(req.getRoutingKey(), payload);
        return Map.of(
                "messageId", messageId,
                "routingKey", req.getRoutingKey(),
                "hint", "Topic Exchange: order.# 与 # 绑定"
        );
    }

    @PostMapping("/demo/dlx")
    public Map<String, Object> sendDlxDemo(@RequestParam(defaultValue = "1") Long userId,
                                           @RequestParam(defaultValue = "9.90") BigDecimal amount) {
        OrderCreatedEvent event = new OrderCreatedEvent(
                0L,
                "DLX-DEMO-" + System.currentTimeMillis(),
                userId,
                amount,
                LocalDateTime.now()
        );
        String messageId = messageProducer.sendDlxDemo(event);
        Map<String, Object> result = new HashMap<>();
        result.put("messageId", messageId);
        result.put("hint", "设置 app.dlx.always-fail=true 可演示 basicNack -> 死信队列");
        return result;
    }

    @PostMapping("/demo/invalid-route")
    public Map<String, Object> invalidRoute() {
        String messageId = messageProducer.sendInvalidRouteDemo();
        return Map.of(
                "messageId", messageId,
                "hint", "routing key 无匹配队列，触发 publisher-returns 回调"
        );
    }

    @GetMapping("/messages/logs")
    public Object messageLogs(@RequestParam(defaultValue = "20") int limit) {
        return messageLogRepository.findRecent(Math.min(limit, 100));
    }
}

@RestControllerAdvice
class GlobalExceptionHandler {

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiError> handleIllegalArgument(IllegalArgumentException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiError(ex.getMessage()));
    }

    @ExceptionHandler(DuplicateKeyException.class)
    public ResponseEntity<ApiError> handleDuplicateKey(DuplicateKeyException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(new ApiError("数据已存在（唯一键冲突）"));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> handleValidation(MethodArgumentNotValidException ex) {
        String msg = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(fe -> fe.getField() + " " + fe.getDefaultMessage())
                .collect(Collectors.joining("; "));
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiError(msg));
    }
}
