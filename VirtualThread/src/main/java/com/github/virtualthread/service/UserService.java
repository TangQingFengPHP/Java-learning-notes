package com.github.virtualthread.service;

import com.github.virtualthread.entity.User;
import com.github.virtualthread.model.UserCreateRequest;
import com.github.virtualthread.model.UserResponse;
import com.github.virtualthread.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    @Transactional
    public UserResponse create(UserCreateRequest request) {
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new DuplicateKeyException("邮箱已存在：" + request.getEmail());
        }

        User user = new User();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setAge(request.getAge());
        user.setStatus("ACTIVE");
        user.setCreatedAt(LocalDateTime.now());

        Long id = userRepository.save(user);
        user.setId(id);
        return UserResponse.from(user);
    }

    public User findById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("用户不存在，id=" + id));
    }

    public List<UserResponse> findAll() {
        return userRepository.findAll().stream()
                .map(UserResponse::from)
                .toList();
    }

    public List<UserResponse> search(String status, String keyword, int page, int size) {
        return userRepository.search(status, keyword, page, size).stream()
                .map(UserResponse::from)
                .toList();
    }

    @Transactional
    public UserResponse disable(Long id) {
        int updated = userRepository.disableById(id);
        if (updated == 0) {
            throw new IllegalArgumentException("用户不存在，id=" + id);
        }
        return UserResponse.from(findById(id));
    }

    @Transactional
    public void deleteById(Long id) {
        int deleted = userRepository.deleteById(id);
        if (deleted == 0) {
            throw new IllegalArgumentException("用户不存在，id=" + id);
        }
    }

    public long countActiveUsers() {
        return userRepository.countByStatus("ACTIVE");
    }
}
