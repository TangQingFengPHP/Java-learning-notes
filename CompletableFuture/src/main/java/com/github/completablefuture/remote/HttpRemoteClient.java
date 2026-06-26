package com.github.completablefuture.remote;

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
public class HttpRemoteClient {

    private final HttpClient httpClient;

    @Value("${app.base-url}")
    private String baseUrl;

    public String getCoupons(Long userId) throws Exception {
        return get("/mock/coupons?userId=" + userId);
    }

    public String getAccount(Long userId) throws Exception {
        return get("/mock/accounts/" + userId);
    }

    public String getConfigFromPrimary() throws Exception {
        return get("/mock/config/primary");
    }

    public String getConfigFromBackup() throws Exception {
        return get("/mock/config/backup");
    }

    private String get(String path) throws Exception {
        HttpRequest request = HttpRequest.newBuilder(URI.create(baseUrl + path))
                .timeout(Duration.ofSeconds(3))
                .GET()
                .build();
        return httpClient.send(request, HttpResponse.BodyHandlers.ofString()).body();
    }
}
