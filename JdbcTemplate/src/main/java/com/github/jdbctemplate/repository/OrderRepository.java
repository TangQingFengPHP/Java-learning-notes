package com.github.jdbctemplate.repository;

import com.github.jdbctemplate.entity.Order;
import com.github.jdbctemplate.model.OrderUserDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class OrderRepository {

    private final JdbcTemplate jdbcTemplate;
    private final NamedParameterJdbcTemplate namedJdbcTemplate;

    public List<Order> findByStatus(String status) {
        String sql = """
                select id, user_id, order_no, amount, status, created_at
                from tb_order
                where status = ?
                order by id desc
                """;
        return jdbcTemplate.query(sql, RowMappers.ORDER, status);
    }

    public List<OrderUserDTO> findOrderUsersByStatus(String status) {
        String sql = """
                select
                    o.id as order_id,
                    o.order_no,
                    o.amount,
                    o.status as order_status,
                    u.id as user_id,
                    u.username,
                    u.email
                from tb_order o
                left join tb_user u on o.user_id = u.id
                where o.status = :status
                order by o.id desc
                """;

        MapSqlParameterSource params = new MapSqlParameterSource("status", status);
        return namedJdbcTemplate.query(sql, params, RowMappers.ORDER_USER);
    }
}
