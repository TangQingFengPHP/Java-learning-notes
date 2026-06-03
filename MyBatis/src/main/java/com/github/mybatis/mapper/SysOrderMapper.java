package com.github.mybatis.mapper;

import com.github.mybatis.entity.SysOrder;
import com.github.mybatis.model.OrderUserDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface SysOrderMapper {

    List<SysOrder> selectByStatus(@Param("status") String status);

    List<OrderUserDTO> selectOrderUsersByStatus(@Param("status") String status);
}
