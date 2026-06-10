package com.github.jdbc.service;

import com.github.jdbc.dao.UserDao;
import com.github.jdbc.entity.User;
import com.github.jdbc.model.PageResult;
import com.github.jdbc.model.UserCreateRequest;
import com.github.jdbc.model.UserSearchRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserDao userDao;

    public Long create(String username, String email, Integer age) {
        User user = buildUser(username, email, age);
        return userDao.registerWithLog(user, "用户注册成功（JDBC 手动事务）");
    }

    public Long createSimple(String username, String email, Integer age) {
        User user = buildUser(username, email, age);
        return userDao.insert(user);
    }

    public Optional<User> findById(Long id) {
        return userDao.findById(id);
    }

    public Optional<User> findByEmail(String email) {
        return userDao.findByEmail(email);
    }

    public List<User> search(UserSearchRequest req) {
        String status = req != null ? req.getStatus() : null;
        Integer minAge = req != null ? req.getMinAge() : null;
        return userDao.search(status, minAge);
    }

    public PageResult<User> page(String status, int pageNumber, int pageSize) {
        long total = userDao.countByStatus(status);
        List<User> records = userDao.findPage(status, pageNumber, pageSize);
        return new PageResult<>(records, total, pageNumber, pageSize);
    }

    public void updateEmail(Long id, String email) {
        int updated = userDao.updateEmail(id, email);
        if (updated == 0) {
            throw new IllegalArgumentException("用户不存在");
        }
    }

    public void remove(Long id) {
        int deleted = userDao.deleteById(id);
        if (deleted == 0) {
            throw new IllegalArgumentException("用户不存在");
        }
    }

    public int[] batchCreate(List<UserCreateRequest> requests) {
        List<User> users = requests.stream()
                .map(req -> buildUser(req.getUsername(), req.getEmail(), req.getAge()))
                .toList();
        return userDao.batchInsert(users);
    }

    private User buildUser(String username, String email, Integer age) {
        User user = new User();
        user.setUsername(username);
        user.setEmail(email);
        user.setAge(age);
        user.setStatus("ACTIVE");
        user.setCreatedAt(LocalDateTime.now());
        return user;
    }
}
