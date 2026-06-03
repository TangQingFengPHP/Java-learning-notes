package com.github.springdatajpa.service;

import com.github.springdatajpa.entity.User;
import com.github.springdatajpa.entity.Order;
import com.github.springdatajpa.model.PageResult;
import com.github.springdatajpa.model.SliceResult;
import com.github.springdatajpa.model.UserSummaryDTO;
import com.github.springdatajpa.model.UserWithOrdersDTO;
import com.github.springdatajpa.projection.UserSummary;
import com.github.springdatajpa.repository.UserRepository;
import com.github.springdatajpa.spec.UserSpecifications;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {

    private final UserRepository userRepository;

    @Transactional
    public Long create(String username, String email, Integer age) {
        User user = new User();
        user.setUsername(username);
        user.setEmail(email);
        user.setAge(age);
        user.setStatus("ACTIVE");
        return userRepository.save(user).getId();
    }

    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }

    public Optional<UserWithOrdersDTO> findWithOrdersById(Long id) {
        return userRepository.findWithOrdersById(id).map(this::toUserWithOrders);
    }

    private UserWithOrdersDTO toUserWithOrders(User user) {
        UserWithOrdersDTO dto = new UserWithOrdersDTO();
        dto.setId(user.getId());
        dto.setUsername(user.getUsername());
        dto.setEmail(user.getEmail());
        dto.setAge(user.getAge());
        dto.setStatus(user.getStatus());
        dto.setOrders(user.getOrders().stream().map(this::toOrderItem).toList());
        return dto;
    }

    private UserWithOrdersDTO.OrderItemDTO toOrderItem(Order order) {
        UserWithOrdersDTO.OrderItemDTO item = new UserWithOrdersDTO.OrderItemDTO();
        item.setId(order.getId());
        item.setOrderNo(order.getOrderNo());
        item.setAmount(order.getAmount());
        item.setStatus(order.getStatus());
        item.setCreatedAt(order.getCreatedAt());
        return item;
    }

    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public List<User> searchByMethodName(String status) {
        return userRepository.findByStatusOrderByIdDesc(status);
    }

    public List<User> findByIds(List<Long> ids) {
        return userRepository.findByIdIn(ids);
    }

    public List<User> findByJpql(String status, Integer minAge) {
        return userRepository.findActiveUsers(status, minAge);
    }

    public List<User> findByNativeSql(String status, Integer minAge) {
        return userRepository.findByNativeSql(status, minAge);
    }

    public List<UserSummary> findSummaries(String status) {
        return userRepository.findSummariesByStatus(status);
    }

    public List<UserSummaryDTO> findSummaryDtos(String status) {
        return userRepository.findSummaryDtoByStatus(status);
    }

    public PageResult<User> page(String keyword, String status, Integer minAge, int pageNumber, int pageSize) {
        Pageable pageable = PageRequest.of(
                pageNumber - 1,
                pageSize,
                Sort.by(Sort.Direction.DESC, "id")
        );
        Page<User> page = userRepository.findAll(
                UserSpecifications.search(keyword, status, minAge),
                pageable
        );
        return PageResult.from(page);
    }

    public PageResult<User> pageByStatus(String status, int pageNumber, int pageSize) {
        Pageable pageable = PageRequest.of(pageNumber - 1, pageSize, Sort.by(Sort.Direction.DESC, "id"));
        Page<User> page = userRepository.findAll(
                UserSpecifications.search(null, status, null),
                pageable
        );
        return PageResult.from(page);
    }

    public SliceResult<User> sliceByStatus(String status, int pageNumber, int pageSize) {
        Pageable pageable = PageRequest.of(pageNumber - 1, pageSize, Sort.by(Sort.Direction.DESC, "id"));
        Slice<User> slice = userRepository.findByStatus(status, pageable);
        return SliceResult.from(slice);
    }

    @Transactional
    public void updateEmail(Long id, String email) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("用户不存在"));
        user.setEmail(email);
    }

    @Transactional
    public void disable(Long id) {
        int updated = userRepository.updateStatus(id, "DISABLED", LocalDateTime.now());
        if (updated == 0) {
            throw new IllegalArgumentException("用户不存在");
        }
    }

    @Transactional
    public void disableByAgeLessThan(int ageExclusive) {
        userRepository.disableByAgeLessThan(ageExclusive, LocalDateTime.now());
    }

    @Transactional
    public void optimisticDisable(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("用户不存在"));
        user.setStatus("DISABLED");
    }

    @Transactional
    public void remove(Long id) {
        if (!userRepository.existsById(id)) {
            throw new IllegalArgumentException("用户不存在");
        }
        userRepository.deleteById(id);
    }
}
