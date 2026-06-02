package com.github.mybatisplus.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.mybatisplus.entity.SysUser;
import com.github.mybatisplus.mapper.SysUserMapper;
import com.github.mybatisplus.service.SysUserService;
import org.springframework.stereotype.Service;

@Service
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements SysUserService {
}

