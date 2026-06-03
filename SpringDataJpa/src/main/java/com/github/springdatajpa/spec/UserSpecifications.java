package com.github.springdatajpa.spec;

import com.github.springdatajpa.entity.User;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public final class UserSpecifications {

    private UserSpecifications() {
    }

    public static Specification<User> search(String keyword, String status, Integer minAge) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (keyword != null && !keyword.isBlank()) {
                Predicate usernameLike = criteriaBuilder.like(root.get("username"), "%" + keyword + "%");
                Predicate emailLike = criteriaBuilder.like(root.get("email"), "%" + keyword + "%");
                predicates.add(criteriaBuilder.or(usernameLike, emailLike));
            }

            if (status != null && !status.isBlank()) {
                predicates.add(criteriaBuilder.equal(root.get("status"), status));
            }

            if (minAge != null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("age"), minAge));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}
