package com.github.completablefuture.service;

import com.github.completablefuture.entity.User;
import com.github.completablefuture.model.UserHomeModels;
import com.github.completablefuture.model.UserHomeModels.AccountInfo;
import com.github.completablefuture.model.UserHomeModels.CouponInfo;
import com.github.completablefuture.model.UserHomeModels.OrderInfo;
import com.github.completablefuture.model.UserHomeModels.UserHomeResponse;
import com.github.completablefuture.model.UserHomeModels.UserInfo;
import com.github.completablefuture.remote.HttpRemoteClient;
import com.github.completablefuture.repository.OrderRepository;
import com.github.completablefuture.repository.UserRepository;
import com.github.completablefuture.support.AsyncMetricsRecorder;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class UserHomeService {

    @Qualifier("queryExecutor")
    private final ExecutorService queryExecutor;
    private final UserRepository userRepository;
    private final OrderRepository orderRepository;
    private final HttpRemoteClient httpRemoteClient;
    private final ObjectMapper objectMapper;
    private final AsyncMetricsRecorder metricsRecorder;

    @Value("${app.home.total-timeout-ms}")
    private long totalTimeoutMs;

    @Value("${app.home.coupon-timeout-ms}")
    private long couponTimeoutMs;

    public CompletableFuture<UserHomeResponse> loadHomeAsync(Long userId) {
        long start = System.currentTimeMillis();

        CompletableFuture<UserInfo> userFuture = CompletableFuture.supplyAsync(
                () -> findUser(userId),
                queryExecutor
        );

        CompletableFuture<AccountInfo> accountFuture = CompletableFuture.supplyAsync(
                () -> findAccount(userId),
                queryExecutor
        );

        CompletableFuture<List<OrderInfo>> orderFuture = CompletableFuture.supplyAsync(
                () -> findRecentOrders(userId),
                queryExecutor
        );

        CompletableFuture<CouponInfo> couponFuture = CompletableFuture.supplyAsync(
                () -> findCoupon(userId),
                queryExecutor
        ).completeOnTimeout(new CouponInfo(0), couponTimeoutMs, TimeUnit.MILLISECONDS);

        return CompletableFuture
                .allOf(userFuture, accountFuture, orderFuture, couponFuture)
                .thenApply(ignore -> new UserHomeResponse(
                        userFuture.join(),
                        accountFuture.join(),
                        orderFuture.join(),
                        couponFuture.join(),
                        System.currentTimeMillis() - start
                ))
                .orTimeout(totalTimeoutMs, TimeUnit.MILLISECONDS)
                .whenComplete((response, ex) -> {
                    if (ex != null) {
                        metricsRecorder.recordFailure("user-home", ex);
                    } else {
                        metricsRecorder.recordSuccess("user-home", "userId=" + userId);
                    }
                });
    }

    public UserHomeResponse loadHome(Long userId) {
        return loadHomeAsync(userId).join();
    }

    private UserInfo findUser(Long userId) {
        sleep(200);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("用户不存在，id=" + userId));
        return UserInfo.from(user);
    }

    private AccountInfo findAccount(Long userId) {
        sleep(300);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("用户不存在，id=" + userId));
        return new AccountInfo(userId, user.getBalance());
    }

    private List<OrderInfo> findRecentOrders(Long userId) {
        sleep(400);
        return orderRepository.findRecentByUserId(userId, 5).stream()
                .map(order -> new OrderInfo(order.getId(), "订单-" + order.getProductId()))
                .toList();
    }

    private CouponInfo findCoupon(Long userId) {
        try {
            String body = httpRemoteClient.getCoupons(userId);
            JsonNode node = objectMapper.readTree(body);
            return new CouponInfo(node.get("count").asInt());
        } catch (Exception e) {
            throw new IllegalStateException("优惠券服务不可用", e);
        }
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
