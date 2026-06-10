package com.github.jdbc.dao;

import com.github.jdbc.model.OrderUserDTO;
import com.github.jdbc.support.JdbcSupport;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class OrderDao {

    private final JdbcSupport jdbcSupport;

    public List<OrderUserDTO> findOrderUserByStatus(String status) {
        String sql = """
                select o.id, o.order_no, o.amount, o.status, u.id, u.username, u.email
                from orders o
                join users u on u.id = o.user_id
                where o.status = ?
                order by o.id desc
                """;

        try (Connection connection = jdbcSupport.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, status);
            List<OrderUserDTO> records = new ArrayList<>();
            try (ResultSet rs = statement.executeQuery()) {
                while (rs.next()) {
                    records.add(new OrderUserDTO(
                            rs.getLong(1),
                            rs.getString(2),
                            rs.getBigDecimal(3),
                            rs.getString(4),
                            rs.getLong(5),
                            rs.getString(6),
                            rs.getString(7)
                    ));
                }
            }
            return records;
        } catch (SQLException ex) {
            throw new IllegalStateException("订单 join 查询失败", ex);
        }
    }
}
