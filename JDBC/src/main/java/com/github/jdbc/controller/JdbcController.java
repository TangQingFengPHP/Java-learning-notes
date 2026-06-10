package com.github.jdbc.controller;

import com.github.jdbc.entity.Account;
import com.github.jdbc.model.OrderUserDTO;
import com.github.jdbc.model.PoolInfoDTO;
import com.github.jdbc.model.TransferRequest;
import com.github.jdbc.service.JdbcMetaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/jdbc")
@RequiredArgsConstructor
public class JdbcController {

    private final JdbcMetaService jdbcMetaService;

    @GetMapping("/pool")
    public PoolInfoDTO poolInfo() {
        return jdbcMetaService.poolInfo();
    }

    @GetMapping("/hello")
    public Map<String, String> hello() {
        return Map.of("message", "Hello JDBC (PreparedStatement + HikariCP)");
    }
}

@RestController
@RequestMapping("/accounts")
@RequiredArgsConstructor
class AccountController {

    private final JdbcMetaService jdbcMetaService;

    @GetMapping
    public List<Account> list() {
        return jdbcMetaService.listAccounts();
    }

    @PostMapping("/transfer")
    public void transfer(@Valid @RequestBody TransferRequest request) {
        jdbcMetaService.transfer(request);
    }
}

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
class OrderController {

    private final JdbcMetaService jdbcMetaService;

    @GetMapping("/join")
    public List<OrderUserDTO> join(@RequestParam(defaultValue = "PAID") String status) {
        return jdbcMetaService.findOrderUserByStatus(status);
    }
}
