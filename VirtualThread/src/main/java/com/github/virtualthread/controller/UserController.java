package com.github.virtualthread.controller;

import com.github.virtualthread.context.TraceContext;
import com.github.virtualthread.model.UserCreateRequest;
import com.github.virtualthread.model.UserResponse;
import com.github.virtualthread.model.UserWithThreadResponse;
import com.github.virtualthread.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserResponse create(@Valid @RequestBody UserCreateRequest request) {
        return userService.create(request);
    }

    @GetMapping("/{id}")
    public UserWithThreadResponse findById(@PathVariable Long id) {
        return UserWithThreadResponse.of(userService.findById(id), TraceContext.get());
    }

    @GetMapping
    public List<UserResponse> findAll() {
        return userService.findAll();
    }

    @GetMapping("/search")
    public List<UserResponse> search(
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String keyword,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return userService.search(status, keyword, page, size);
    }

    @PutMapping("/{id}/disable")
    public UserResponse disable(@PathVariable Long id) {
        return userService.disable(id);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteById(@PathVariable Long id) {
        userService.deleteById(id);
    }
}
