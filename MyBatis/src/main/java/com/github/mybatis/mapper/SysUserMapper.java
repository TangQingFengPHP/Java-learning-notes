package com.github.mybatis.mapper;

import com.github.mybatis.entity.SysUser;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface SysUserMapper {

    int insert(SysUser user);

    int update(SysUser user);

    int logicDeleteById(@Param("id") Long id);

    SysUser selectById(@Param("id") Long id);

    List<SysUser> search(@Param("keyword") String keyword,
                         @Param("status") String status,
                         @Param("minAge") Integer minAge);

    List<SysUser> selectByIds(@Param("ids") List<Long> ids);

    List<SysUser> selectPage(@Param("status") String status,
                             @Param("offset") int offset,
                             @Param("size") int size);

    long countByStatus(@Param("status") String status);

    int disableByAgeLessThan(@Param("age") int age);
}
