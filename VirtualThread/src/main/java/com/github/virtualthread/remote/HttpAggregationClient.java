package com.github.virtualthread.remote;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

@Component
@RequiredArgsConstructor
public class HttpAggregationClient {

    private final HttpClient httpClient;

    @Value("${app.base-url}")
    private String baseUrl;

    public String getOrders(Long userId) throws Exception {
        return get("/mock/orders?userId=" + userId);
    }

    public String getAccount(Long userId) throws Exception {
        return get("/mock/accounts/" + userId);
    }

    private String get(String path) throws Exception {
        HttpRequest request = HttpRequest.newBuilder(URI.create(baseUrl + path))
                .timeout(Duration.ofSeconds(3))
                .GET()
                .build();
        return httpClient.send(request, HttpResponse.BodyHandlers.ofString()).body();
    }
}
