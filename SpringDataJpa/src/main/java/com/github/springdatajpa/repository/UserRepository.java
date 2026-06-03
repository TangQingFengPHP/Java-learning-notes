package com.github.springdatajpa.repository;

import com.github.springdatajpa.entity.User;
import com.github.springdatajpa.model.UserSummaryDTO;
import com.github.springdatajpa.projection.UserSummary;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long>, JpaSpecificationExecutor<User> {

    Optional<User> findByEmail(String email);

    List<User> findByStatus(String status);

    List<User> findByStatusOrderByIdDesc(String status);

    List<User> findByIdIn(List<Long> ids);

    boolean existsByEmail(String email);

    long countByStatus(String status);

    Slice<User> findByStatus(String status, Pageable pageable);

    List<UserSummary> findSummariesByStatus(String status);

    @EntityGraph(attributePaths = "orders")
    Optional<User> findWithOrdersById(Long id);

    @Query("""
            select u
            from User u
            where u.status = :status
              and u.age >= :minAge
            order by u.id desc
            """)
    List<User> findActiveUsers(@Param("status") String status, @Param("minAge") Integer minAge);

    @Query(value = """
            select *
            from users
            where status = :status
              and age >= :minAge
            order by id desc
            """, nativeQuery = true)
    List<User> findByNativeSql(@Param("status") String status, @Param("minAge") Integer minAge);

    @Query("""
            select new com.github.springdatajpa.model.UserSummaryDTO(u.id, u.username, u.email)
            from User u
            where u.status = :status
            order by u.id desc
            """)
    List<UserSummaryDTO> findSummaryDtoByStatus(@Param("status") String status);

    @Modifying
    @Query("""
            update User u
            set u.status = :status,
                u.updatedAt = :updatedAt
            where u.id = :id
            """)
    int updateStatus(@Param("id") Long id,
                     @Param("status") String status,
                     @Param("updatedAt") LocalDateTime updatedAt);

    @Modifying
    @Query("""
            update User u
            set u.status = 'DISABLED',
                u.updatedAt = :updatedAt
            where u.age < :age
            """)
    int disableByAgeLessThan(@Param("age") int age, @Param("updatedAt") LocalDateTime updatedAt);
}
