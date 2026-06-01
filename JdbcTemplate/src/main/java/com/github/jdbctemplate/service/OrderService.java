package com.github.jdbctemplate.service;

import com.github.jdbctemplate.entity.Order;
import com.github.jdbctemplate.model.OrderUserDTO;
import com.github.jdbctemplate.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;

    public List<Order> listByStatus(String status) {
        return orderRepository.findByStatus(status);
    }

    public List<OrderUserDTO> findOrderUsersByStatus(String status) {
        return orderRepository.findOrderUsersByStatus(status);
    }
}
