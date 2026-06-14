package com.github.webflux.repository;

import com.github.webflux.entity.User;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface UserRepository extends ReactiveCrudRepository<User, Long> {

    Mono<User> findByEmail(String email);

    Flux<User> findByStatus(String status);

    Mono<Long> countByStatus(String status);

    @Query("""
            SELECT *
            FROM tb_user
            WHERE (:status IS NULL OR status = :status)
              AND (:keyword IS NULL OR username LIKE CONCAT('%', :keyword, '%'))
            ORDER BY id DESC
            LIMIT :size OFFSET :offset
            """)
    Flux<User> search(String status, String keyword, int size, long offset);
}
