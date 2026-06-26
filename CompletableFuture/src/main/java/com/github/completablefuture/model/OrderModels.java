package com.github.completablefuture.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

public final class OrderModels {

    private OrderModels() {
    }

    public record OrderCreateCommand(
            @NotNull Long userId,
            @NotNull Long productId,
            @NotNull @Min(1) Integer quantity
    ) {
    }

    public record StockInfo(Long productId, Integer available) {
    }

    public record PriceInfo(Long productId, BigDecimal price) {
    }

    public record StockResult(Boolean success, Integer remaining) {
    }

    @Data
    @AllArgsConstructor
    public static class OrderCreateResult {
        private Long orderId;
        private Long userId;
        private Long productId;
        private BigDecimal unitPrice;
        private BigDecimal totalAmount;
        private Boolean stockDeducted;
        private long costMs;
    }
}
