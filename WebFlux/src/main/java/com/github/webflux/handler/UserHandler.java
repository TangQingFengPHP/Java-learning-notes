package com.github.webflux.handler;

import com.github.webflux.model.UserCreateRequest;
import com.github.webflux.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class UserHandler {

    private final UserService userService;

    public Mono<ServerResponse> findById(ServerRequest request) {
        Long id = Long.valueOf(request.pathVariable("id"));
        return userService.findById(id)
                .flatMap(user -> ServerResponse.ok().bodyValue(user));
    }

    public Mono<ServerResponse> create(ServerRequest request) {
        return request.bodyToMono(UserCreateRequest.class)
                .flatMap(userService::create)
                .flatMap(user -> ServerResponse
                        .created(request.uriBuilder().path("/{id}").build(user.getId()))
                        .bodyValue(user));
    }

    public Mono<ServerResponse> stream(ServerRequest request) {
        return ServerResponse.ok()
                .contentType(MediaType.TEXT_EVENT_STREAM)
                .body(userService.findAll(), com.github.webflux.model.UserResponse.class);
    }
}
