package com.github.completablefuture.model;

import com.github.completablefuture.entity.Product;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class ProductBatchResponse {
    private List<Product> products;
    private long costMs;
}
