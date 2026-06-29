package com.github.mavenpractice.service.repository;

import com.github.mavenpractice.domain.model.OrderUserDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class OrderRepository {

    private final NamedParameterJdbcTemplate namedJdbcTemplate;

    public List<OrderUserDTO> findOrdersWithUser(String status) {
        String sql = """
                select o.id as order_id, o.order_no, o.status as order_status,
                       u.id as user_id, u.username, u.email
                from tb_order o
                inner join tb_user u on o.user_id = u.id
                where o.status = :status
                order by o.id desc
                """;

        return namedJdbcTemplate.query(sql, new MapSqlParameterSource("status", status), (rs, rowNum) -> {
            OrderUserDTO dto = new OrderUserDTO();
            dto.setOrderId(rs.getLong("order_id"));
            dto.setOrderNo(rs.getString("order_no"));
            dto.setOrderStatus(rs.getString("order_status"));
            dto.setUserId(rs.getLong("user_id"));
            dto.setUsername(rs.getString("username"));
            dto.setEmail(rs.getString("email"));
            return dto;
        });
    }
}
