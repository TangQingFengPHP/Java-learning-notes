package com.github.flyway.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class UserOrderSummaryDTO {
    private Long userId;
    private String username;
    private Long orderCount;
    private BigDecimal totalAmount;
}
