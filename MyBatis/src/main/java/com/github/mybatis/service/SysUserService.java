package com.github.mybatis.service;

import com.github.mybatis.entity.SysUser;
import com.github.mybatis.mapper.SysUserMapper;
import com.github.mybatis.model.PageResult;
import com.github.mybatis.model.SysUserSearchRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SysUserService {

    private final SysUserMapper sysUserMapper;

    @Transactional
    public Long create(String username, String email, Integer age) {
        SysUser user = new SysUser();
        user.setUsername(username);
        user.setEmail(email);
        user.setAge(age);
        user.setStatus("ACTIVE");
        user.setDeleted(0);
        user.setCreatedAt(LocalDateTime.now());
        sysUserMapper.insert(user);
        return user.getId();
    }

    public Optional<SysUser> findById(Long id) {
        return Optional.ofNullable(sysUserMapper.selectById(id));
    }

    public List<SysUser> search(SysUserSearchRequest req) {
        if (req == null) {
            return sysUserMapper.search(null, null, null);
        }
        return sysUserMapper.search(req.getKeyword(), req.getStatus(), req.getMinAge());
    }

    public PageResult<SysUser> page(String status, int pageNumber, int pageSize) {
        int offset = (pageNumber - 1) * pageSize;
        long total = sysUserMapper.countByStatus(status);
        List<SysUser> records = sysUserMapper.selectPage(status, offset, pageSize);
        return new PageResult<>(records, total, pageNumber, pageSize);
    }

    public List<SysUser> findByIds(List<Long> ids) {
        return sysUserMapper.selectByIds(ids);
    }

    @Transactional
    public void updateEmail(Long id, String email) {
        SysUser user = new SysUser();
        user.setId(id);
        user.setEmail(email);
        user.setUpdatedAt(LocalDateTime.now());
        int updated = sysUserMapper.update(user);
        if (updated == 0) {
            throw new IllegalArgumentException("用户不存在或已被删除");
        }
    }

    @Transactional
    public void disableByAgeLessThan(int ageExclusive) {
        sysUserMapper.disableByAgeLessThan(ageExclusive);
    }

    @Transactional
    public void remove(Long id) {
        int deleted = sysUserMapper.logicDeleteById(id);
        if (deleted == 0) {
            throw new IllegalArgumentException("用户不存在或已被删除");
        }
    }
}
