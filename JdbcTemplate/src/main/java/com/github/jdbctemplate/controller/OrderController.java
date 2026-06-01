package com.github.jdbctemplate.controller;

import com.github.jdbctemplate.entity.Order;
import com.github.jdbctemplate.model.OrderUserDTO;
import com.github.jdbctemplate.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @GetMapping
    public List<Order> listByStatus(@RequestParam(defaultValue = "PAID") String status) {
        return orderService.listByStatus(status);
    }

    @GetMapping("/join")
    public List<OrderUserDTO> join(@RequestParam(defaultValue = "PAID") String status) {
        return orderService.findOrderUsersByStatus(status);
    }
}
