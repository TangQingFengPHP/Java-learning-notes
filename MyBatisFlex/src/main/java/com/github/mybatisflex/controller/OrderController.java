package com.github.mybatisflex.controller;

import com.github.mybatisflex.entity.Order;
import com.github.mybatisflex.model.OrderUserDTO;
import com.github.mybatisflex.service.OrderService;
import com.mybatisflex.core.row.Row;
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
        return orderService.listOrdersByStatus(status);
    }

    @GetMapping("/join/wrapper")
    public List<OrderUserDTO> joinByWrapper(@RequestParam(defaultValue = "PAID") String status) {
        return orderService.findOrderUsersByStatusWithWrapper(status);
    }

    @GetMapping("/join/xml")
    public List<OrderUserDTO> joinByXml(@RequestParam(defaultValue = "PAID") String status) {
        return orderService.findOrderUsersByStatusWithXml(status);
    }

    @GetMapping("/rows")
    public List<Row> rowsBySql(@RequestParam(defaultValue = "PAID") String status) {
        return orderService.listOrdersBySql(status);
    }
}

