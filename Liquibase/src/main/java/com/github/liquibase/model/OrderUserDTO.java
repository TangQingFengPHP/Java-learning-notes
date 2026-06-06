package com.github.liquibase.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class OrderUserDTO {
    private Long orderId;
    private String orderNo;
    private BigDecimal amount;
    private String orderStatus;
    private Long userId;
    private String username;
    private String email;
}
