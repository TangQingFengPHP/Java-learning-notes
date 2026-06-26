package com.github.completablefuture.model;

import com.github.completablefuture.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

public final class UserHomeModels {

    private UserHomeModels() {
    }

    public record UserInfo(Long userId, String username) {
        public static UserInfo from(User user) {
            return new UserInfo(user.getId(), user.getUsername());
        }
    }

    public record AccountInfo(Long userId, BigDecimal balance) {
    }

    public record OrderInfo(Long orderId, String title) {
    }

    public record CouponInfo(Integer count) {
    }

    @Data
    @AllArgsConstructor
    public static class UserHomeResponse {
        private UserInfo user;
        private AccountInfo account;
        private List<OrderInfo> orders;
        private CouponInfo coupon;
        private long costMs;
    }
}
