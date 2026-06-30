package com.github.rabbitmq.repository;

import com.github.rabbitmq.model.Order;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class OrderRepository {

    private final JdbcTemplate jdbcTemplate;

    public Long save(String orderNo, Long userId, java.math.BigDecimal amount, String status, LocalDateTime createdAt) {
        String sql = "insert into tb_order (order_no, user_id, amount, status, created_at) values (?, ?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, orderNo);
            ps.setLong(2, userId);
            ps.setBigDecimal(3, amount);
            ps.setString(4, status);
            ps.setObject(5, createdAt);
            return ps;
        }, keyHolder);
        Number key = keyHolder.getKey();
        return key == null ? null : key.longValue();
    }

    public Optional<Order> findById(Long id) {
        String sql = "select id, order_no, user_id, amount, status, created_at from tb_order where id = ?";
        var list = jdbcTemplate.query(sql, (rs, rowNum) -> {
            Order order = new Order();
            order.setId(rs.getLong("id"));
            order.setOrderNo(rs.getString("order_no"));
            order.setUserId(rs.getLong("user_id"));
            order.setAmount(rs.getBigDecimal("amount"));
            order.setStatus(rs.getString("status"));
            order.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
            return order;
        }, id);
        return list.isEmpty() ? Optional.empty() : Optional.of(list.getFirst());
    }

    public int updateStatus(Long id, String status) {
        return jdbcTemplate.update("update tb_order set status = ? where id = ?", status, id);
    }
}
