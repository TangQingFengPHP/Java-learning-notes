package com.github.rabbitmq.model;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class OrderCreateRequest {

    @NotNull
    private Long userId;

    @NotNull
    @DecimalMin("0.01")
    private BigDecimal amount;
}
