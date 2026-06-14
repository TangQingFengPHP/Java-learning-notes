package com.github.webflux;

import com.github.webflux.model.UserResponse;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

class ReactorTest {

    @Test
    void shouldFilterActiveUserNames() {
        Flux<String> names = Flux.just(
                        buildUser(1L, "张三", "ACTIVE"),
                        buildUser(2L, "李四", "DISABLED"),
                        buildUser(3L, "王五", "ACTIVE")
                )
                .filter(user -> "ACTIVE".equals(user.getStatus()))
                .map(UserResponse::getUsername);

        StepVerifier.create(names)
                .expectNext("张三")
                .expectNext("王五")
                .verifyComplete();
    }

    private static UserResponse buildUser(Long id, String username, String status) {
        UserResponse user = new UserResponse();
        user.setId(id);
        user.setUsername(username);
        user.setEmail(username + "@example.com");
        user.setAge(20);
        user.setStatus(status);
        return user;
    }
}
