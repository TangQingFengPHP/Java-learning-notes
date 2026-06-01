package com.github.jdbctemplate.service;

import com.github.jdbctemplate.entity.User;
import com.github.jdbctemplate.model.PageResult;
import com.github.jdbctemplate.model.UserSearchRequest;
import com.github.jdbctemplate.repository.RegisterLogRepository;
import com.github.jdbctemplate.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final RegisterLogRepository registerLogRepository;

    @Transactional
    public Long register(String username, String email, Integer age) {
        User user = new User();
        user.setUsername(username);
        user.setEmail(email);
        user.setAge(age);
        user.setStatus("ACTIVE");
        user.setCreatedAt(LocalDateTime.now());

        Long userId = userRepository.saveAndReturnId(user);
        registerLogRepository.insert(userId, "用户注册成功");
        return userId;
    }

    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }

    public String findEmailById(Long id) {
        return userRepository.findEmailById(id);
    }

    public long countAll() {
        return userRepository.countAll();
    }

    public List<User> search(UserSearchRequest req) {
        return userRepository.search(req);
    }

    public PageResult<User> page(String status, int pageNumber, int pageSize) {
        long total = userRepository.countByStatus(status);
        List<User> records = userRepository.findPageByStatus(status, pageNumber, pageSize);
        return new PageResult<>(records, total, pageNumber, pageSize);
    }

    public List<User> pageByJdbc(int pageNumber, int pageSize) {
        return userRepository.findPage(pageNumber, pageSize);
    }

    public List<User> findByIds(List<Long> ids) {
        return userRepository.findByIds(ids);
    }

    public List<User> findAllByBeanMapper() {
        return userRepository.findAllByBeanPropertyMapper();
    }

    @Transactional
    public void updateEmail(Long id, String email) {
        int updated = userRepository.updateEmail(id, email, LocalDateTime.now());
        if (updated == 0) {
            throw new IllegalArgumentException("用户不存在");
        }
    }

    @Transactional
    public void disableByAgeLessThan(int ageExclusive) {
        userRepository.disableByAgeLessThan(ageExclusive);
    }

    @Transactional
    public void remove(Long id) {
        int deleted = userRepository.deleteById(id);
        if (deleted == 0) {
            throw new IllegalArgumentException("用户不存在");
        }
    }

    @Transactional
    public int[] batchUpdateStatus(List<Long> ids, String status) {
        return userRepository.batchUpdateStatus(ids, status);
    }
}
