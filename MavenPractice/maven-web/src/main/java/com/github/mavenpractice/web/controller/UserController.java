package com.github.mavenpractice.web.controller;

import com.github.mavenpractice.common.PageResult;
import com.github.mavenpractice.domain.entity.User;
import com.github.mavenpractice.domain.model.BatchUpdateStatusRequest;
import com.github.mavenpractice.domain.model.UserCreateRequest;
import com.github.mavenpractice.domain.model.UserSearchRequest;
import com.github.mavenpractice.domain.model.UserUpdateEmailRequest;
import com.github.mavenpractice.service.UserService;
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
import java.util.Map;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping
    public Long register(@Valid @RequestBody UserCreateRequest req) {
        return userService.register(req.getUsername(), req.getEmail(), req.getAge());
    }

    @GetMapping("/{id}")
    public User detail(@PathVariable Long id) {
        return userService.findById(id).orElseThrow(() -> new IllegalArgumentException("用户不存在"));
    }

    @GetMapping("/{id}/email")
    public Map<String, String> email(@PathVariable Long id) {
        String email = userService.findEmailById(id);
        if (email == null) {
            throw new IllegalArgumentException("用户不存在");
        }
        return Map.of("email", email);
    }

    @GetMapping("/count")
    public Map<String, Long> count() {
        return Map.of("total", userService.countAll());
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

    @GetMapping("/by-ids")
    public List<User> findByIds(@RequestParam String ids) {
        List<Long> idList = Arrays.stream(ids.split(","))
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .map(Long::valueOf)
                .toList();
        return userService.findByIds(idList);
    }

    @PutMapping("/{id}/email")
    public void updateEmail(@PathVariable Long id, @Valid @RequestBody UserUpdateEmailRequest req) {
        userService.updateEmail(id, req.getEmail());
    }

    @PutMapping("/batch-status")
    public int[] batchStatus(@Valid @RequestBody BatchUpdateStatusRequest req) {
        return userService.batchUpdateStatus(req.getIds(), req.getStatus());
    }

    @DeleteMapping("/{id}")
    public void remove(@PathVariable Long id) {
        userService.remove(id);
    }
}
