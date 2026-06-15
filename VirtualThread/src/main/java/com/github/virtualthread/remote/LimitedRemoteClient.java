package com.github.virtualthread.remote;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.concurrent.Semaphore;

@Component
@RequiredArgsConstructor
public class LimitedRemoteClient {

    private final HttpClient httpClient;
    private final Semaphore remoteSemaphore;

    @Value("${app.base-url}")
    private String baseUrl;

    public String callSlowEndpoint(String path) throws Exception {
        remoteSemaphore.acquire();
        try {
            HttpRequest request = HttpRequest.newBuilder(URI.create(baseUrl + path))
                    .timeout(Duration.ofSeconds(3))
                    .GET()
                    .build();
            return httpClient.send(request, HttpResponse.BodyHandlers.ofString()).body();
        } finally {
            remoteSemaphore.release();
        }
    }
}
