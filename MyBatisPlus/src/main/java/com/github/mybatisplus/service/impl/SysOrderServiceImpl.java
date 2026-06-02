package com.github.mybatisplus.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.mybatisplus.entity.SysOrder;
import com.github.mybatisplus.mapper.SysOrderMapper;
import com.github.mybatisplus.service.SysOrderService;
import org.springframework.stereotype.Service;

@Service
public class SysOrderServiceImpl extends ServiceImpl<SysOrderMapper, SysOrder> implements SysOrderService {
}

