package com.github.virtualthread.controller;

import com.github.virtualthread.model.UserDetailResponse;
import com.github.virtualthread.service.UserDetailService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserDetailController {

    private final UserDetailService userDetailService;

    @GetMapping("/{id}/detail")
    public UserDetailResponse detail(@PathVariable Long id) throws Exception {
        return userDetailService.loadDetail(id);
    }
}
