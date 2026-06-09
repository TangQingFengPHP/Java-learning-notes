package com.github.tomcat.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

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
