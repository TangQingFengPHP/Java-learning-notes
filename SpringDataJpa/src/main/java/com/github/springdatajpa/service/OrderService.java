package com.github.springdatajpa.service;

import com.github.springdatajpa.model.OrderUserDTO;
import com.github.springdatajpa.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class OrderService {

    private final OrderRepository orderRepository;

    public List<OrderUserDTO> findOrderUserByStatus(String status) {
        return orderRepository.findOrderUserByStatus(status);
    }
}
