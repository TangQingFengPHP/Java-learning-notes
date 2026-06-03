package com.github.springdatajpa.model;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class UserWithOrdersDTO {
    private Long id;
    private String username;
    private String email;
    private Integer age;
    private String status;
    private List<OrderItemDTO> orders;

    @Data
    public static class OrderItemDTO {
        private Long id;
        private String orderNo;
        private BigDecimal amount;
        private String status;
        private LocalDateTime createdAt;
    }
}
