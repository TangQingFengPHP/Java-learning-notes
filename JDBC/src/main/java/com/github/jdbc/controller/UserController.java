package com.github.jdbc.controller;

import com.github.jdbc.entity.User;
import com.github.jdbc.model.PageResult;
import com.github.jdbc.model.UserCreateRequest;
import com.github.jdbc.model.UserSearchRequest;
import com.github.jdbc.model.UserUpdateEmailRequest;
import com.github.jdbc.service.UserService;
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

    @PostMapping("/simple")
    public Long createSimple(@Valid @RequestBody UserCreateRequest req) {
        return userService.createSimple(req.getUsername(), req.getEmail(), req.getAge());
    }

    @PostMapping("/batch")
    public int[] batchCreate(@Valid @RequestBody List<UserCreateRequest> requests) {
        return userService.batchCreate(requests);
    }

    @GetMapping("/{id}")
    public User detail(@PathVariable Long id) {
        return userService.findById(id).orElseThrow(() -> new IllegalArgumentException("用户不存在"));
    }

    @GetMapping("/by-email")
    public User findByEmail(@RequestParam String email) {
        return userService.findByEmail(email).orElseThrow(() -> new IllegalArgumentException("用户不存在"));
    }

    @PostMapping("/search")
    public List<User> search(@Valid @RequestBody(required = false) UserSearchRequest req) {
        return userService.search(req);
    }

    @GetMapping
    public PageResult<User> page(@RequestParam(defaultValue = "ACTIVE") String status,
                                 @RequestParam(defaultValue = "1") int pageNumber,
                                 @RequestParam(defaultValue = "10") int pageSize) {
        return userService.page(status, pageNumber, pageSize);
    }

    @PutMapping("/{id}/email")
    public void updateEmail(@PathVariable Long id, @Valid @RequestBody UserUpdateEmailRequest req) {
        userService.updateEmail(id, req.getEmail());
    }

    @DeleteMapping("/{id}")
    public void remove(@PathVariable Long id) {
        userService.remove(id);
    }
}
