package com.github.completablefuture.service;

import com.github.completablefuture.model.OrderModels.OrderCreateCommand;
import com.github.completablefuture.model.OrderModels.OrderCreateResult;
import com.github.completablefuture.model.OrderModels.PriceInfo;
import com.github.completablefuture.model.OrderModels.StockResult;
import com.github.completablefuture.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class OrderPersistService {

    private final OrderRepository orderRepository;

    @Transactional
    public OrderCreateResult persist(
            OrderCreateCommand command,
            PriceInfo priceInfo,
            StockResult stockResult,
            long start) {
        BigDecimal totalAmount = priceInfo.price().multiply(BigDecimal.valueOf(command.quantity()));
        Long orderId = orderRepository.save(
                command.userId(),
                command.productId(),
                command.quantity(),
                priceInfo.price(),
                totalAmount
        );

        return new OrderCreateResult(
                orderId,
                command.userId(),
                command.productId(),
                priceInfo.price(),
                totalAmount,
                stockResult.success(),
                System.currentTimeMillis() - start
        );
    }
}
