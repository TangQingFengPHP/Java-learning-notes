package com.github.webflux.controller;

import com.github.webflux.model.UserResponse;
import com.github.webflux.service.UserDetailService;
import com.github.webflux.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import static org.mockito.Mockito.when;

@WebFluxTest(UserController.class)
class UserControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockitoBean
    private UserService userService;

    @MockitoBean
    private UserDetailService userDetailService;

    @Test
    void shouldFindUserById() {
        UserResponse response = new UserResponse();
        response.setId(1L);
        response.setUsername("张三");
        response.setEmail("zhangsan@example.com");
        response.setAge(20);
        response.setStatus("ACTIVE");

        when(userService.findById(1L)).thenReturn(Mono.just(response));

        webTestClient.get()
                .uri("/api/users/1")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.id").isEqualTo(1)
                .jsonPath("$.username").isEqualTo("张三")
                .jsonPath("$.status").isEqualTo("ACTIVE");
    }
}
