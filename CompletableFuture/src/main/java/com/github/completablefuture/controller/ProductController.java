package com.github.completablefuture.controller;

import com.github.completablefuture.model.ProductBatchResponse;
import com.github.completablefuture.service.ProductBatchService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductBatchService productBatchService;

    @GetMapping("/batch")
    public ProductBatchResponse batch(@RequestParam String ids) {
        List<Long> productIds = Arrays.stream(ids.split(","))
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .map(Long::valueOf)
                .toList();
        return productBatchService.loadBatch(productIds);
    }
}
