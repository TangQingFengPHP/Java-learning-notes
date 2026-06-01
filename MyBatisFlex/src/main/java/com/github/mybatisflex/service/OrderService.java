package com.github.mybatisflex.service;

import com.github.mybatisflex.entity.Order;
import com.github.mybatisflex.mapper.OrderMapper;
import com.github.mybatisflex.model.OrderUserDTO;
import com.mybatisflex.core.query.QueryWrapper;
import com.mybatisflex.core.row.Db;
import com.mybatisflex.core.row.Row;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.github.mybatisflex.entity.table.OrderTableDef.ORDER;
import static com.github.mybatisflex.entity.table.UserTableDef.USER;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderMapper orderMapper;

    public List<OrderUserDTO> findOrderUsersByStatusWithWrapper(String status) {
        QueryWrapper query = QueryWrapper.create()
                .select(
                        ORDER.ID.as("orderId"),
                        ORDER.ORDER_NO.as("orderNo"),
                        ORDER.AMOUNT,
                        ORDER.STATUS.as("orderStatus"),
                        USER.ID.as("userId"),
                        USER.USERNAME.as("username"),
                        USER.EMAIL.as("email")
                )
                .from(ORDER)
                .leftJoin(USER).on(ORDER.USER_ID.eq(USER.ID))
                .where(ORDER.STATUS.eq(status))
                .orderBy(ORDER.ID.desc());
        return orderMapper.selectListByQueryAs(query, OrderUserDTO.class);
    }

    public List<OrderUserDTO> findOrderUsersByStatusWithXml(String status) {
        return orderMapper.selectOrderUsersByStatus(status);
    }

    public List<Row> listOrdersBySql(String status) {
        return Db.selectListBySql(
                "select id, user_id, order_no, amount, status, created_at from tb_order where status = ? order by id desc",
                status
        );
    }

    public List<Order> listOrdersByStatus(String status) {
        QueryWrapper query = QueryWrapper.create().where(ORDER.STATUS.eq(status)).orderBy(ORDER.ID.desc());
        return orderMapper.selectListByQuery(query);
    }
}

