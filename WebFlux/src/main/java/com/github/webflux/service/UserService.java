package com.github.webflux.service;

import com.github.webflux.entity.User;
import com.github.webflux.exception.EmailAlreadyExistsException;
import com.github.webflux.exception.UserNotFoundException;
import com.github.webflux.model.UserCreateRequest;
import com.github.webflux.model.UserResponse;
import com.github.webflux.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public Mono<UserResponse> create(UserCreateRequest request) {
        return userRepository.findByEmail(request.getEmail())
                .flatMap(user -> Mono.<User>error(new EmailAlreadyExistsException(request.getEmail())))
                .switchIfEmpty(Mono.defer(() -> {
                    User user = new User();
                    user.setUsername(request.getUsername());
                    user.setEmail(request.getEmail());
                    user.setAge(request.getAge());
                    user.setStatus("ACTIVE");
                    user.setCreatedAt(LocalDateTime.now());
                    return userRepository.save(user);
                }))
                .map(UserResponse::from);
    }

    public Mono<UserResponse> findById(Long id) {
        return userRepository.findById(id)
                .switchIfEmpty(Mono.error(new UserNotFoundException(id)))
                .map(UserResponse::from);
    }

    public Flux<UserResponse> findAll() {
        return userRepository.findAll()
                .map(UserResponse::from);
    }

    public Flux<UserResponse> search(String status, String keyword, int page, int size) {
        long offset = (long) Math.max(page - 1, 0) * size;
        return userRepository.search(status, keyword, size, offset)
                .map(UserResponse::from);
    }

    public Mono<UserResponse> disable(Long id) {
        return userRepository.findById(id)
                .switchIfEmpty(Mono.error(new UserNotFoundException(id)))
                .flatMap(user -> {
                    user.setStatus("DISABLED");
                    return userRepository.save(user);
                })
                .map(UserResponse::from);
    }

    public Mono<Void> deleteById(Long id) {
        return userRepository.findById(id)
                .switchIfEmpty(Mono.error(new UserNotFoundException(id)))
                .flatMap(userRepository::delete);
    }

    public Mono<Long> countActiveUsers() {
        return userRepository.countByStatus("ACTIVE");
    }
}
