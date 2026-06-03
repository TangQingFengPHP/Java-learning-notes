package com.github.springdatajpa.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
public class OrderUserDTO {
    private Long orderId;
    private String orderNo;
    private BigDecimal amount;
    private String orderStatus;
    private Long userId;
    private String username;
    private String email;

    public OrderUserDTO(Long orderId, String orderNo, BigDecimal amount, String orderStatus,
                        Long userId, String username, String email) {
        this.orderId = orderId;
        this.orderNo = orderNo;
        this.amount = amount;
        this.orderStatus = orderStatus;
        this.userId = userId;
        this.username = username;
        this.email = email;
    }
}
