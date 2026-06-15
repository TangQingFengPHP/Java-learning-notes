package com.github.virtualthread.service;

import com.github.virtualthread.model.UserDetailResponse;
import com.github.virtualthread.model.UserResponse;
import com.github.virtualthread.remote.HttpAggregationClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

@Service
@RequiredArgsConstructor
public class UserDetailService {

    private final UserService userService;
    private final HttpAggregationClient httpAggregationClient;
    private final ExecutorService virtualThreadExecutor;

    public UserDetailResponse loadDetail(Long userId) throws Exception {
        long start = System.currentTimeMillis();

        Future<UserResponse> userFuture = virtualThreadExecutor.submit(() ->
                UserResponse.from(userService.findById(userId))
        );

        Future<String> orderFuture = virtualThreadExecutor.submit(() ->
                httpAggregationClient.getOrders(userId)
        );

        Future<String> accountFuture = virtualThreadExecutor.submit(() ->
                httpAggregationClient.getAccount(userId)
        );

        Future<Long> countFuture = virtualThreadExecutor.submit(userService::countActiveUsers);

        return new UserDetailResponse(
                userFuture.get(),
                orderFuture.get(),
                accountFuture.get(),
                countFuture.get(),
                System.currentTimeMillis() - start
        );
    }
}
