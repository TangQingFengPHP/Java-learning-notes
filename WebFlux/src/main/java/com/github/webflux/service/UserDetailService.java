package com.github.webflux.service;

import com.github.webflux.model.UserDetailResponse;
import com.github.webflux.remote.RemoteUserClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class UserDetailService {

    private final UserService userService;
    private final RemoteUserClient remoteUserClient;

    public Mono<UserDetailResponse> findDetail(Long userId) {
        Mono<com.github.webflux.model.UserResponse> userMono = userService.findById(userId);
        Mono<com.github.webflux.model.RemoteUserSnapshot> remoteMono = remoteUserClient.findById(userId);
        Mono<Long> activeCountMono = userService.countActiveUsers();

        return Mono.zip(userMono, remoteMono, activeCountMono)
                .map(tuple -> new UserDetailResponse(tuple.getT1(), tuple.getT2(), tuple.getT3()));
    }
}
