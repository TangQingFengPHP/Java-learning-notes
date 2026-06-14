package com.github.webflux.remote;

import com.github.webflux.exception.RemoteServiceException;
import com.github.webflux.exception.RemoteUserNotFoundException;
import com.github.webflux.model.RemoteUserSnapshot;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.Duration;

@Component
@RequiredArgsConstructor
public class RemoteUserClient {

    private final WebClient userApiClient;

    public Mono<RemoteUserSnapshot> findById(Long id) {
        return userApiClient.get()
                .uri("/api/users/{id}", id)
                .retrieve()
                .onStatus(
                        status -> status.value() == 404,
                        response -> Mono.error(new RemoteUserNotFoundException(id))
                )
                .onStatus(
                        status -> status.is5xxServerError(),
                        response -> response.bodyToMono(String.class)
                                .defaultIfEmpty("")
                                .flatMap(body -> Mono.error(new RemoteServiceException(body)))
                )
                .bodyToMono(RemoteUserSnapshot.class)
                .timeout(Duration.ofSeconds(2));
    }
}
