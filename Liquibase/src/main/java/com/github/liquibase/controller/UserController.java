package com.github.liquibase.controller;

import com.github.liquibase.entity.User;
import com.github.liquibase.model.UserCreateRequest;
import com.github.liquibase.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
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
        return userService.create(req.getUsername(), req.getEmail(), req.getAge(), req.getPhone());
    }

    @GetMapping("/{id}")
    public User detail(@PathVariable Long id) {
        return userService.findById(id).orElseThrow(() -> new IllegalArgumentException("用户不存在"));
    }

    @GetMapping
    public List<User> list() {
        return userService.findAll();
    }

    @GetMapping("/by-email")
    public User findByEmail(@RequestParam String email) {
        return userService.findByEmail(email).orElseThrow(() -> new IllegalArgumentException("用户不存在"));
    }
}
