package com.github.mybatisflex.service;

import com.github.mybatisflex.entity.User;
import com.github.mybatisflex.mapper.UserMapper;
import com.github.mybatisflex.model.UserSearchRequest;
import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.query.QueryWrapper;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static com.github.mybatisflex.entity.table.UserTableDef.USER;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserMapper userMapper;

    @Transactional
    public Long create(String username, String email, Integer age) {
        User user = new User();
        user.setUsername(username);
        user.setEmail(email);
        user.setAge(age);
        user.setStatus("ACTIVE");
        user.setDeleted(false);
        user.setCreatedAt(LocalDateTime.now());
        userMapper.insert(user);
        return user.getId();
    }

    public Optional<User> findById(Long id) {
        return Optional.ofNullable(userMapper.selectOneById(id));
    }

    public List<User> search(UserSearchRequest req) {
        QueryWrapper query = QueryWrapper.create()
                .select()
                .from(USER);

        if (req != null) {
            if (req.getUsername() != null && !req.getUsername().isBlank()) {
                query.and(USER.USERNAME.like(req.getUsername()));
            }
            if (req.getStatus() != null && !req.getStatus().isBlank()) {
                query.and(USER.STATUS.eq(req.getStatus()));
            }
            if (req.getMinAge() != null) {
                query.and(USER.AGE.ge(req.getMinAge()));
            }
        }

        query.orderBy(USER.ID.desc());
        return userMapper.selectListByQuery(query);
    }

    public Page<User> page(String status, int pageNumber, int pageSize) {
        QueryWrapper query = QueryWrapper.create()
                .where(USER.STATUS.eq(status))
                .orderBy(USER.ID.desc());
        return userMapper.paginate(pageNumber, pageSize, query);
    }

    @Transactional
    public void updateEmail(Long id, String email) {
        User user = userMapper.selectOneById(id);
        if (user == null) {
            throw new IllegalArgumentException("用户不存在或已被删除");
        }
        user.setEmail(email);
        user.setUpdatedAt(LocalDateTime.now());
        int updated = userMapper.update(user);
        if (updated == 0) {
            throw new OptimisticLockingFailureException("乐观锁冲突：记录已被其他事务修改");
        }
    }

    @Transactional
    public void disableByAgeLessThan(int ageExclusive) {
        User updateEntity = new User();
        updateEntity.setStatus("DISABLED");
        updateEntity.setUpdatedAt(LocalDateTime.now());

        QueryWrapper query = QueryWrapper.create()
                .where(USER.AGE.lt(ageExclusive));
        userMapper.updateByQuery(updateEntity, query);
    }

    @Transactional
    public void remove(Long id) {
        int deleted = userMapper.deleteById(id);
        if (deleted == 0) {
            throw new IllegalArgumentException("用户不存在或已被删除");
        }
    }

    @Transactional
    public void optimisticDisable(Long id) {
        User user = userMapper.selectOneById(id);
        if (user == null) {
            throw new IllegalArgumentException("用户不存在或已被删除");
        }
        user.setStatus("DISABLED");
        user.setUpdatedAt(LocalDateTime.now());
        int updated = userMapper.update(user);
        if (updated == 0) {
            throw new OptimisticLockingFailureException("乐观锁冲突：记录已被其他事务修改");
        }
    }
}

