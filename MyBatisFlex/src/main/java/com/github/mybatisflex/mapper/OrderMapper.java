package com.github.mybatisflex.mapper;

import com.github.mybatisflex.entity.Order;
import com.github.mybatisflex.model.OrderUserDTO;
import com.mybatisflex.core.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface OrderMapper extends BaseMapper<Order> {

    List<OrderUserDTO> selectOrderUsersByStatus(@Param("status") String status);
}

