package com.github.springdatajpa.controller;

import com.github.springdatajpa.entity.User;
import com.github.springdatajpa.model.PageResult;
import com.github.springdatajpa.model.SliceResult;
import com.github.springdatajpa.model.UserCreateRequest;
import com.github.springdatajpa.model.UserSearchRequest;
import com.github.springdatajpa.model.UserSummaryDTO;
import com.github.springdatajpa.model.UserUpdateEmailRequest;
import com.github.springdatajpa.model.UserWithOrdersDTO;
import com.github.springdatajpa.projection.UserSummary;
import com.github.springdatajpa.service.UserService;
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
public class UserController {

    private final UserService userService;

    @PostMapping
    public Long create(@Valid @RequestBody UserCreateRequest req) {
        return userService.create(req.getUsername(), req.getEmail(), req.getAge());
    }

    @GetMapping("/{id}")
    public User detail(@PathVariable Long id) {
        return userService.findById(id).orElseThrow(() -> new IllegalArgumentException("用户不存在"));
    }

    @GetMapping("/{id}/with-orders")
    public UserWithOrdersDTO detailWithOrders(@PathVariable Long id) {
        return userService.findWithOrdersById(id).orElseThrow(() -> new IllegalArgumentException("用户不存在"));
    }

    @GetMapping("/by-email")
    public User findByEmail(@RequestParam String email) {
        return userService.findByEmail(email).orElseThrow(() -> new IllegalArgumentException("用户不存在"));
    }

    @GetMapping("/method-name")
    public List<User> searchByMethodName(@RequestParam(defaultValue = "ACTIVE") String status) {
        return userService.searchByMethodName(status);
    }

    @PostMapping("/search")
    public PageResult<User> searchPage(@Valid @RequestBody(required = false) UserSearchRequest req,
                                       @RequestParam(defaultValue = "1") int pageNumber,
                                       @RequestParam(defaultValue = "10") int pageSize) {
        String keyword = req != null ? req.getKeyword() : null;
        String status = req != null ? req.getStatus() : null;
        Integer minAge = req != null ? req.getMinAge() : null;
        return userService.page(keyword, status, minAge, pageNumber, pageSize);
    }

    @GetMapping
    public PageResult<User> page(@RequestParam(defaultValue = "ACTIVE") String status,
                                 @RequestParam(defaultValue = "1") int pageNumber,
                                 @RequestParam(defaultValue = "10") int pageSize) {
        return userService.pageByStatus(status, pageNumber, pageSize);
    }

    @GetMapping("/slice")
    public SliceResult<User> slice(@RequestParam(defaultValue = "ACTIVE") String status,
                                   @RequestParam(defaultValue = "1") int pageNumber,
                                   @RequestParam(defaultValue = "10") int pageSize) {
        return userService.sliceByStatus(status, pageNumber, pageSize);
    }

    @GetMapping("/jpql")
    public List<User> findByJpql(@RequestParam(defaultValue = "ACTIVE") String status,
                                 @RequestParam(defaultValue = "18") Integer minAge) {
        return userService.findByJpql(status, minAge);
    }

    @GetMapping("/native")
    public List<User> findByNative(@RequestParam(defaultValue = "ACTIVE") String status,
                                   @RequestParam(defaultValue = "18") Integer minAge) {
        return userService.findByNativeSql(status, minAge);
    }

    @GetMapping("/summary")
    public List<UserSummary> summaries(@RequestParam(defaultValue = "ACTIVE") String status) {
        return userService.findSummaries(status);
    }

    @GetMapping("/summary-dto")
    public List<UserSummaryDTO> summaryDtos(@RequestParam(defaultValue = "ACTIVE") String status) {
        return userService.findSummaryDtos(status);
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

    @PutMapping("/{id}/disable")
    public void disable(@PathVariable Long id) {
        userService.disable(id);
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
