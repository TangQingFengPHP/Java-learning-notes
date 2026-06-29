package com.github.mavenpractice.web.controller;

import com.github.mavenpractice.domain.model.OrderUserDTO;
import com.github.mavenpractice.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @GetMapping("/join")
    public List<OrderUserDTO> joinPaid() {
        return orderService.listPaidOrders();
    }
}
