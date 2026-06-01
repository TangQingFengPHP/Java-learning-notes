package com.github.jdbctemplate.repository;

import com.github.jdbctemplate.entity.Order;
import com.github.jdbctemplate.entity.User;
import com.github.jdbctemplate.model.OrderUserDTO;
import org.springframework.jdbc.core.RowMapper;

import java.sql.Timestamp;

public final class RowMappers {

    private RowMappers() {
    }

    public static final RowMapper<User> USER = (rs, rowNum) -> {
        User user = new User();
        user.setId(rs.getLong("id"));
        user.setUsername(rs.getString("username"));
        user.setEmail(rs.getString("email"));
        user.setAge(rs.getInt("age"));
        user.setStatus(rs.getString("status"));
        Timestamp createdAt = rs.getTimestamp("created_at");
        if (createdAt != null) {
            user.setCreatedAt(createdAt.toLocalDateTime());
        }
        Timestamp updatedAt = rs.getTimestamp("updated_at");
        if (updatedAt != null) {
            user.setUpdatedAt(updatedAt.toLocalDateTime());
        }
        return user;
    };

    public static final RowMapper<Order> ORDER = (rs, rowNum) -> {
        Order order = new Order();
        order.setId(rs.getLong("id"));
        order.setUserId(rs.getLong("user_id"));
        order.setOrderNo(rs.getString("order_no"));
        order.setAmount(rs.getBigDecimal("amount"));
        order.setStatus(rs.getString("status"));
        Timestamp createdAt = rs.getTimestamp("created_at");
        if (createdAt != null) {
            order.setCreatedAt(createdAt.toLocalDateTime());
        }
        return order;
    };

    public static final RowMapper<OrderUserDTO> ORDER_USER = (rs, rowNum) -> {
        OrderUserDTO dto = new OrderUserDTO();
        dto.setOrderId(rs.getLong("order_id"));
        dto.setOrderNo(rs.getString("order_no"));
        dto.setAmount(rs.getBigDecimal("amount"));
        dto.setOrderStatus(rs.getString("order_status"));
        dto.setUserId(rs.getLong("user_id"));
        dto.setUsername(rs.getString("username"));
        dto.setEmail(rs.getString("email"));
        return dto;
    };

    public static String userSelectColumns() {
        return "id, username, email, age, status, created_at, updated_at";
    }
}
