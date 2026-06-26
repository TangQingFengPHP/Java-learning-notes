package com.github.completablefuture.repository;

import com.github.completablefuture.entity.Order;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class OrderRepository {

    private final JdbcTemplate jdbcTemplate;

    public List<Order> findRecentByUserId(Long userId, int limit) {
        String sql = """
                select %s from tb_order
                where user_id = ?
                order by created_at desc
                limit ?
                """.formatted(RowMappers.orderSelectColumns());
        return jdbcTemplate.query(sql, RowMappers.ORDER, userId, limit);
    }

    public Long save(Long userId, Long productId, int quantity, BigDecimal unitPrice, BigDecimal totalAmount) {
        String sql = """
                insert into tb_order (user_id, product_id, quantity, unit_price, total_amount, status, created_at)
                values (?, ?, ?, ?, ?, ?, ?)
                """;

        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setLong(1, userId);
            ps.setLong(2, productId);
            ps.setInt(3, quantity);
            ps.setBigDecimal(4, unitPrice);
            ps.setBigDecimal(5, totalAmount);
            ps.setString(6, "PAID");
            ps.setObject(7, LocalDateTime.now());
            return ps;
        }, keyHolder);

        Number key = keyHolder.getKey();
        return key == null ? null : key.longValue();
    }
}
