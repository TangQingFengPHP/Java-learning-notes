package com.github.mavenpractice.service;

import com.github.mavenpractice.domain.model.OrderUserDTO;
import com.github.mavenpractice.service.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;

    public List<OrderUserDTO> listPaidOrders() {
        return orderRepository.findOrdersWithUser("PAID");
    }
}
