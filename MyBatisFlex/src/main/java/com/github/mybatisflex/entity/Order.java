package com.github.mybatisflex.entity;

import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.KeyType;
import com.mybatisflex.annotation.Table;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Table("tb_order")
public class Order {

    @Id(keyType = KeyType.Auto)
    private Long id;

    private Long userId;

    private String orderNo;

    private BigDecimal amount;

    private String status;

    private LocalDateTime createdAt;
}

