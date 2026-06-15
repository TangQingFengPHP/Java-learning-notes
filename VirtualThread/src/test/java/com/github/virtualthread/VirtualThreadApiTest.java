package com.github.virtualthread;

import org.junit.jupiter.api.Test;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import static org.junit.jupiter.api.Assertions.assertTrue;

class VirtualThreadApiTest {

    @Test
    void shouldRunTaskOnVirtualThread() throws Exception {
        try (ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor()) {
            Future<Boolean> future = executor.submit(() -> Thread.currentThread().isVirtual());
            assertTrue(future.get());
        }
    }

    @Test
    void startVirtualThreadShouldBeVirtual() throws InterruptedException {
        Thread[] holder = new Thread[1];
        Thread thread = Thread.startVirtualThread(() -> holder[0] = Thread.currentThread());
        thread.join();
        assertTrue(holder[0].isVirtual());
    }
}
