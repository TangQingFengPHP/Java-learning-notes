package com.github.tomcat.controller;

import com.github.tomcat.model.OrderUserDTO;
import com.github.tomcat.service.OrderService;
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

    @GetMapping("/join")
    public List<OrderUserDTO> join(@RequestParam(defaultValue = "PAID") String status) {
        return orderService.findOrderUserByStatus(status);
    }
}
