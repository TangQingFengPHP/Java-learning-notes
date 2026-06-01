package com.github.mybatisflex.model;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class OrderUserDTO {
    private Long orderId;
    private String orderNo;
    private BigDecimal amount;
    private String orderStatus;

    private Long userId;
    private String username;
    private String email;
}

