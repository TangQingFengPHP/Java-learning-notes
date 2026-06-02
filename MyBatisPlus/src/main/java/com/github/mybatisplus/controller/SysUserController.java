package com.github.mybatisplus.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.mybatisplus.entity.SysUser;
import com.github.mybatisplus.model.SysUserCreateRequest;
import com.github.mybatisplus.model.SysUserSearchRequest;
import com.github.mybatisplus.model.SysUserUpdateEmailRequest;
import com.github.mybatisplus.service.SysUserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class SysUserController {

    private final SysUserService sysUserService;

    @PostMapping
    public Long create(@Valid @RequestBody SysUserCreateRequest req) {
        SysUser user = new SysUser();
        user.setUsername(req.getUsername());
        user.setEmail(req.getEmail());
        user.setAge(req.getAge());
        user.setStatus("ACTIVE");
        sysUserService.save(user);
        return user.getId();
    }

    @GetMapping("/{id}")
    public SysUser detail(@PathVariable Long id) {
        SysUser user = sysUserService.getById(id);
        if (user == null) {
            throw new IllegalArgumentException("用户不存在或已被删除");
        }
        return user;
    }

    @PostMapping("/search")
    public List<SysUser> search(@Valid @RequestBody(required = false) SysUserSearchRequest req) {
        LambdaQueryWrapper<SysUser> qw = new LambdaQueryWrapper<>();
        if (req != null) {
            if (req.getUsername() != null && !req.getUsername().isBlank()) {
                qw.like(SysUser::getUsername, req.getUsername());
            }
            if (req.getStatus() != null && !req.getStatus().isBlank()) {
                qw.eq(SysUser::getStatus, req.getStatus());
            }
            if (req.getMinAge() != null) {
                qw.ge(SysUser::getAge, req.getMinAge());
            }
        }
        qw.orderByDesc(SysUser::getId);
        return sysUserService.list(qw);
    }

    @GetMapping
    public Page<SysUser> page(@RequestParam(defaultValue = "ACTIVE") String status,
                              @RequestParam(defaultValue = "1") long pageNumber,
                              @RequestParam(defaultValue = "10") long pageSize) {
        Page<SysUser> page = Page.of(pageNumber, pageSize);
        LambdaQueryWrapper<SysUser> qw = new LambdaQueryWrapper<SysUser>()
                .eq(SysUser::getStatus, status)
                .orderByDesc(SysUser::getId);
        return sysUserService.page(page, qw);
    }

    @PutMapping("/{id}/email")
    public void updateEmail(@PathVariable Long id, @Valid @RequestBody SysUserUpdateEmailRequest req) {
        LambdaUpdateWrapper<SysUser> uw = new LambdaUpdateWrapper<SysUser>()
                .eq(SysUser::getId, id)
                .set(SysUser::getEmail, req.getEmail());
        boolean ok = sysUserService.update(uw);
        if (!ok) {
            throw new IllegalArgumentException("用户不存在或已被删除");
        }
    }

    @PutMapping("/disable-by-age")
    public void disableByAge(@RequestParam(defaultValue = "18") int ltAge) {
        LambdaUpdateWrapper<SysUser> uw = new LambdaUpdateWrapper<SysUser>()
                .lt(SysUser::getAge, ltAge)
                .set(SysUser::getStatus, "DISABLED");
        sysUserService.update(uw);
    }

    @PutMapping("/{id}/optimistic-disable")
    public void optimisticDisable(@PathVariable Long id) {
        SysUser user = sysUserService.getById(id);
        if (user == null) {
            throw new IllegalArgumentException("用户不存在或已被删除");
        }
        user.setStatus("DISABLED");
        boolean ok = sysUserService.updateById(user);
        if (!ok) {
            throw new OptimisticLockingFailureException("乐观锁冲突：记录已被其他事务修改");
        }
    }

    @DeleteMapping("/{id}")
    public void remove(@PathVariable Long id) {
        boolean ok = sysUserService.removeById(id);
        if (!ok) {
            throw new IllegalArgumentException("用户不存在或已被删除");
        }
    }
}

