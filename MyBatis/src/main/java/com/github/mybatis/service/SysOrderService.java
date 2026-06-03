package com.github.mybatis.service;

import com.github.mybatis.entity.SysOrder;
import com.github.mybatis.mapper.SysOrderMapper;
import com.github.mybatis.model.OrderUserDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SysOrderService {

    private final SysOrderMapper sysOrderMapper;

    public List<SysOrder> listByStatus(String status) {
        return sysOrderMapper.selectByStatus(status);
    }

    public List<OrderUserDTO> findOrderUsersByStatus(String status) {
        return sysOrderMapper.selectOrderUsersByStatus(status);
    }
}
