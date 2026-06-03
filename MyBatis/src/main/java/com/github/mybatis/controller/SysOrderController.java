package com.github.mybatis.controller;

import com.github.mybatis.entity.SysOrder;
import com.github.mybatis.model.OrderUserDTO;
import com.github.mybatis.service.SysOrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class SysOrderController {

    private final SysOrderService sysOrderService;

    @GetMapping
    public List<SysOrder> list(@RequestParam(defaultValue = "PAID") String status) {
        return sysOrderService.listByStatus(status);
    }

    @GetMapping("/join/xml")
    public List<OrderUserDTO> joinByXml(@RequestParam(defaultValue = "PAID") String status) {
        return sysOrderService.findOrderUsersByStatus(status);
    }
}
