package com.github.springdatajpa.repository;

import com.github.springdatajpa.entity.Order;
import com.github.springdatajpa.model.OrderUserDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {

    @Query("""
            select new com.github.springdatajpa.model.OrderUserDTO(
                o.id, o.orderNo, o.amount, o.status,
                u.id, u.username, u.email
            )
            from Order o
            join o.user u
            where o.status = :status
            order by o.id desc
            """)
    List<OrderUserDTO> findOrderUserByStatus(@Param("status") String status);
}
