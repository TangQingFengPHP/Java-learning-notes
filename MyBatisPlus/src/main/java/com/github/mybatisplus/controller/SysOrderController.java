package com.github.mybatisplus.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.github.mybatisplus.mapper.OrderJoinMapper;
import com.github.mybatisplus.mapper.SysOrderMapper;
import com.github.mybatisplus.entity.SysOrder;
import com.github.mybatisplus.entity.SysUser;
import com.github.mybatisplus.model.OrderUserDTO;
import com.github.mybatisplus.service.SysOrderService;
import com.github.mybatisplus.service.SysUserService;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
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
    private final SysUserService sysUserService;
    private final SysOrderMapper sysOrderMapper;
    private final OrderJoinMapper orderJoinMapper;

    @GetMapping
    public List<SysOrder> list(@RequestParam(defaultValue = "PAID") String status) {
        LambdaQueryWrapper<SysOrder> qw = new LambdaQueryWrapper<SysOrder>()
                .eq(SysOrder::getStatus, status)
                .orderByDesc(SysOrder::getId);
        return sysOrderService.list(qw);
    }

    @GetMapping("/join/wrapper")
    public List<OrderUserDTO> joinByWrapper(@RequestParam(defaultValue = "PAID") String status) {
        MPJLambdaWrapper<SysOrder> wrapper = new MPJLambdaWrapper<SysOrder>()
                .selectAs(SysOrder::getId, OrderUserDTO::getOrderId)
                .selectAs(SysOrder::getOrderNo, OrderUserDTO::getOrderNo)
                .select(SysOrder::getAmount)
                .selectAs(SysOrder::getStatus, OrderUserDTO::getOrderStatus)
                .selectAs(SysUser::getId, OrderUserDTO::getUserId)
                .selectAs(SysUser::getUsername, OrderUserDTO::getUsername)
                .selectAs(SysUser::getEmail, OrderUserDTO::getEmail)
                .leftJoin(SysUser.class, SysUser::getId, SysOrder::getUserId)
                .eq(SysOrder::getStatus, status)
                .orderByDesc(SysOrder::getId);
        return sysOrderMapper.selectJoinList(OrderUserDTO.class, wrapper);
    }

    @GetMapping("/join/xml")
    public List<OrderUserDTO> joinByXml(@RequestParam(defaultValue = "PAID") String status) {
        return orderJoinMapper.selectOrderUsersByStatus(status);
    }
}

