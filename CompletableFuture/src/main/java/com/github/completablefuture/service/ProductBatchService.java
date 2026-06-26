package com.github.completablefuture.service;

import com.github.completablefuture.entity.Product;
import com.github.completablefuture.model.ProductBatchResponse;
import com.github.completablefuture.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;

@Service
@RequiredArgsConstructor
public class ProductBatchService {

    @Qualifier("queryExecutor")
    private final ExecutorService queryExecutor;
    private final ProductRepository productRepository;

    public ProductBatchResponse loadBatch(List<Long> productIds) {
        long start = System.currentTimeMillis();

        List<CompletableFuture<Product>> futures = productIds.stream()
                .map(this::findProduct)
                .toList();

        CompletableFuture<Void> allFuture = CompletableFuture.allOf(
                futures.toArray(CompletableFuture[]::new)
        );

        List<Product> products = allFuture
                .thenApply(ignore -> futures.stream()
                        .map(CompletableFuture::join)
                        .toList())
                .join();

        return new ProductBatchResponse(products, System.currentTimeMillis() - start);
    }

    private CompletableFuture<Product> findProduct(Long productId) {
        return CompletableFuture.supplyAsync(() -> {
            sleep(250);
            return productRepository.findById(productId)
                    .orElseThrow(() -> new IllegalArgumentException("商品不存在，id=" + productId));
        }, queryExecutor);
    }

    private void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException(e);
        }
    }
}
