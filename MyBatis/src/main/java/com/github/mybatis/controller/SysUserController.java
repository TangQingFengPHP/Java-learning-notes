package com.github.mybatis.controller;

import com.github.mybatis.entity.SysUser;
import com.github.mybatis.model.PageResult;
import com.github.mybatis.model.SysUserCreateRequest;
import com.github.mybatis.model.SysUserSearchRequest;
import com.github.mybatis.model.SysUserUpdateEmailRequest;
import com.github.mybatis.service.SysUserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class SysUserController {

    private final SysUserService sysUserService;

    @PostMapping
    public Long create(@Valid @RequestBody SysUserCreateRequest req) {
        return sysUserService.create(req.getUsername(), req.getEmail(), req.getAge());
    }

    @GetMapping("/{id}")
    public SysUser detail(@PathVariable Long id) {
        return sysUserService.findById(id).orElseThrow(() -> new IllegalArgumentException("用户不存在或已被删除"));
    }

    @PostMapping("/search")
    public List<SysUser> search(@Valid @RequestBody(required = false) SysUserSearchRequest req) {
        return sysUserService.search(req);
    }

    @GetMapping
    public PageResult<SysUser> page(@RequestParam(defaultValue = "ACTIVE") String status,
                                    @RequestParam(defaultValue = "1") int pageNumber,
                                    @RequestParam(defaultValue = "10") int pageSize) {
        return sysUserService.page(status, pageNumber, pageSize);
    }

    @GetMapping("/by-ids")
    public List<SysUser> findByIds(@RequestParam String ids) {
        List<Long> idList = Arrays.stream(ids.split(","))
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .map(Long::valueOf)
                .toList();
        return sysUserService.findByIds(idList);
    }

    @PutMapping("/{id}/email")
    public void updateEmail(@PathVariable Long id, @Valid @RequestBody SysUserUpdateEmailRequest req) {
        sysUserService.updateEmail(id, req.getEmail());
    }

    @PutMapping("/disable-by-age")
    public void disableByAge(@RequestParam(defaultValue = "18") int ltAge) {
        sysUserService.disableByAgeLessThan(ltAge);
    }

    @DeleteMapping("/{id}")
    public void remove(@PathVariable Long id) {
        sysUserService.remove(id);
    }
}
