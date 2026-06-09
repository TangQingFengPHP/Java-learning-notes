package com.github.tomcat.service;

import com.github.tomcat.model.OrderUserDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class OrderService {

    private final JdbcTemplate jdbcTemplate;

    public List<OrderUserDTO> findOrderUserByStatus(String status) {
        return jdbcTemplate.query("""
                        select o.id, o.order_no, o.amount, o.status, u.id, u.username, u.email
                        from orders o
                        join users u on u.id = o.user_id
                        where o.status = ?
                        order by o.id desc
                        """,
                (rs, rowNum) -> new OrderUserDTO(
                        rs.getLong(1),
                        rs.getString(2),
                        rs.getBigDecimal(3),
                        rs.getString(4),
                        rs.getLong(5),
                        rs.getString(6),
                        rs.getString(7)
                ),
                status);
    }
}
