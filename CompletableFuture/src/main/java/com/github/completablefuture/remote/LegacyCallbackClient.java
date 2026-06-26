package com.github.completablefuture.remote;

import org.springframework.stereotype.Component;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * 模拟老式回调式异步客户端（如早期 Netty / 自定义 SDK），仅支持 onSuccess / onError 回调。
 */
@Component
public class LegacyCallbackClient {

    private final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor(r -> {
        Thread thread = new Thread(r);
        thread.setName("legacy-callback-scheduler");
        thread.setDaemon(true);
        return thread;
    });

    public void fetchUserProfileAsync(Long userId, boolean fail, long delayMs, LegacyCallback<String> callback) {
        scheduler.schedule(() -> {
            if (fail) {
                callback.onError(new IllegalStateException("老式 SDK 调用失败，userId=" + userId));
                return;
            }
            callback.onSuccess("profile:userId=" + userId + ",name=张三");
        }, delayMs, TimeUnit.MILLISECONDS);
    }

    public void shutdown() {
        scheduler.shutdown();
    }
}
