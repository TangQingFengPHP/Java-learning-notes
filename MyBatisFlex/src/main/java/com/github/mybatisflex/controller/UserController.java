package com.github.mybatisflex.controller;

import com.github.mybatisflex.entity.User;
import com.github.mybatisflex.model.UserCreateRequest;
import com.github.mybatisflex.model.UserSearchRequest;
import com.github.mybatisflex.model.UserUpdateEmailRequest;
import com.github.mybatisflex.service.UserService;
import com.mybatisflex.core.paginate.Page;
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

import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping
    public Long create(@Valid @RequestBody UserCreateRequest req) {
        return userService.create(req.getUsername(), req.getEmail(), req.getAge());
    }

    @GetMapping("/{id}")
    public User detail(@PathVariable Long id) {
        return userService.findById(id).orElseThrow(() -> new IllegalArgumentException("用户不存在或已被删除"));
    }

    @PostMapping("/search")
    public List<User> search(@Valid @RequestBody(required = false) UserSearchRequest req) {
        return userService.search(req);
    }

    @GetMapping
    public Page<User> page(@RequestParam(defaultValue = "ACTIVE") String status,
                           @RequestParam(defaultValue = "1") int pageNumber,
                           @RequestParam(defaultValue = "10") int pageSize) {
        return userService.page(status, pageNumber, pageSize);
    }

    @PutMapping("/{id}/email")
    public void updateEmail(@PathVariable Long id, @Valid @RequestBody UserUpdateEmailRequest req) {
        userService.updateEmail(id, req.getEmail());
    }

    @PutMapping("/disable-by-age")
    public void disableByAge(@RequestParam(defaultValue = "18") int ltAge) {
        userService.disableByAgeLessThan(ltAge);
    }

    @PutMapping("/{id}/optimistic-disable")
    public void optimisticDisable(@PathVariable Long id) {
        userService.optimisticDisable(id);
    }

    @DeleteMapping("/{id}")
    public void remove(@PathVariable Long id) {
        userService.remove(id);
    }
}

