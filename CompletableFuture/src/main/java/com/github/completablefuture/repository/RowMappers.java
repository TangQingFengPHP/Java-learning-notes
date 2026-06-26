package com.github.completablefuture.repository;

import com.github.completablefuture.entity.Order;
import com.github.completablefuture.entity.Product;
import com.github.completablefuture.entity.User;
import org.springframework.jdbc.core.RowMapper;

import java.math.BigDecimal;
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
        user.setBalance(rs.getBigDecimal("balance"));
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

    public static final RowMapper<Product> PRODUCT = (rs, rowNum) -> {
        Product product = new Product();
        product.setId(rs.getLong("id"));
        product.setName(rs.getString("name"));
        product.setPrice(rs.getBigDecimal("price"));
        product.setStock(rs.getInt("stock"));
        product.setStatus(rs.getString("status"));
        Timestamp createdAt = rs.getTimestamp("created_at");
        if (createdAt != null) {
            product.setCreatedAt(createdAt.toLocalDateTime());
        }
        return product;
    };

    public static final RowMapper<Order> ORDER = (rs, rowNum) -> {
        Order order = new Order();
        order.setId(rs.getLong("id"));
        order.setUserId(rs.getLong("user_id"));
        order.setProductId(rs.getLong("product_id"));
        order.setQuantity(rs.getInt("quantity"));
        order.setUnitPrice(rs.getBigDecimal("unit_price"));
        order.setTotalAmount(rs.getBigDecimal("total_amount"));
        order.setStatus(rs.getString("status"));
        Timestamp createdAt = rs.getTimestamp("created_at");
        if (createdAt != null) {
            order.setCreatedAt(createdAt.toLocalDateTime());
        }
        return order;
    };

    public static String userSelectColumns() {
        return "id, username, email, age, status, balance, created_at, updated_at";
    }

    public static String productSelectColumns() {
        return "id, name, price, stock, status, created_at";
    }

    public static String orderSelectColumns() {
        return "id, user_id, product_id, quantity, unit_price, total_amount, status, created_at";
    }
}
