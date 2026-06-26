package com.github.completablefuture.service;

import com.github.completablefuture.entity.Product;
import com.github.completablefuture.entity.User;
import com.github.completablefuture.model.OrderModels.OrderCreateCommand;
import com.github.completablefuture.model.OrderModels.OrderCreateResult;
import com.github.completablefuture.model.OrderModels.PriceInfo;
import com.github.completablefuture.model.OrderModels.StockInfo;
import com.github.completablefuture.model.OrderModels.StockResult;
import com.github.completablefuture.model.UserHomeModels.UserInfo;
import com.github.completablefuture.repository.ProductRepository;
import com.github.completablefuture.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;

@Service
@RequiredArgsConstructor
public class OrderCreateService {

    @Qualifier("queryExecutor")
    private final ExecutorService queryExecutor;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final OrderPersistService orderPersistService;

    public CompletableFuture<OrderCreateResult> createAsync(OrderCreateCommand command) {
        long start = System.currentTimeMillis();

        return validate(command)
                .thenCompose(validCommand -> {
                    CompletableFuture<UserInfo> userFuture = findUser(validCommand.userId());
                    CompletableFuture<StockInfo> stockFuture = checkStock(validCommand.productId());
                    CompletableFuture<PriceInfo> priceFuture = findPrice(validCommand.productId());

                    return CompletableFuture
                            .allOf(userFuture, stockFuture, priceFuture)
                            .thenCompose(ignore -> deductStock(
                                    validCommand.productId(),
                                    validCommand.quantity(),
                                    stockFuture.join()
                            ))
                            .thenApply(stockResult -> orderPersistService.persist(
                                    validCommand,
                                    priceFuture.join(),
                                    stockResult,
                                    start
                            ));
                });
    }

    public OrderCreateResult create(OrderCreateCommand command) {
        return createAsync(command).join();
    }

    private CompletableFuture<OrderCreateCommand> validate(OrderCreateCommand command) {
        return CompletableFuture.supplyAsync(() -> {
            if (command.quantity() <= 0) {
                throw new IllegalArgumentException("商品数量需要大于 0");
            }
            return command;
        }, queryExecutor);
    }

    private CompletableFuture<UserInfo> findUser(Long userId) {
        return CompletableFuture.supplyAsync(() -> {
            sleep(150);
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new IllegalArgumentException("用户不存在，id=" + userId));
            return new UserInfo(user.getId(), user.getUsername());
        }, queryExecutor);
    }

    private CompletableFuture<StockInfo> checkStock(Long productId) {
        return CompletableFuture.supplyAsync(() -> {
            sleep(200);
            Product product = productRepository.findById(productId)
                    .orElseThrow(() -> new IllegalArgumentException("商品不存在，id=" + productId));
            return new StockInfo(productId, product.getStock());
        }, queryExecutor);
    }

    private CompletableFuture<PriceInfo> findPrice(Long productId) {
        return CompletableFuture.supplyAsync(() -> {
            sleep(180);
            Product product = productRepository.findById(productId)
                    .orElseThrow(() -> new IllegalArgumentException("商品不存在，id=" + productId));
            return new PriceInfo(productId, product.getPrice());
        }, queryExecutor);
    }

    private CompletableFuture<StockResult> deductStock(Long productId, int quantity, StockInfo stockInfo) {
        return CompletableFuture.supplyAsync(() -> {
            if (stockInfo.available() < quantity) {
                throw new IllegalStateException("库存不足");
            }
            int updated = productRepository.deductStock(productId, quantity);
            if (updated == 0) {
                throw new IllegalStateException("库存扣减失败");
            }
            return new StockResult(true, stockInfo.available() - quantity);
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
