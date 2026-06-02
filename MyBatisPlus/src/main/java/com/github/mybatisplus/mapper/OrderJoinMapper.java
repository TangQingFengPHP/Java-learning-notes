package com.github.mybatisplus.mapper;

import com.github.mybatisplus.model.OrderUserDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface OrderJoinMapper {
    List<OrderUserDTO> selectOrderUsersByStatus(@Param("status") String status);
}

