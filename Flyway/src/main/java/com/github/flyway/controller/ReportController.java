package com.github.flyway.controller;

import com.github.flyway.entity.Product;
import com.github.flyway.model.UserOrderSummaryDTO;
import com.github.flyway.service.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/reports")
@RequiredArgsConstructor
public class ReportController {

    private final ReportService reportService;

    @GetMapping("/user-order-summary")
    public List<UserOrderSummaryDTO> userOrderSummary() {
        return reportService.userOrderSummary();
    }

    @GetMapping("/config")
    public Map<String, String> config(@RequestParam(defaultValue = "app.name") String key) {
        String value = reportService.getConfigValue(key)
                .orElseThrow(() -> new IllegalArgumentException("配置不存在: " + key));
        return Map.of("key", key, "value", value);
    }

    @PostMapping("/products")
    public Long createProduct(@RequestParam String name, @RequestParam BigDecimal price) {
        return reportService.createProduct(name, price);
    }

    @GetMapping("/products")
    public List<Product> listProducts() {
        return reportService.listProducts();
    }
}
