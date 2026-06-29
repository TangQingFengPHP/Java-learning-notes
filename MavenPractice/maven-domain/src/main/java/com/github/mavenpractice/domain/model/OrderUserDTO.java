package com.github.mavenpractice.domain.model;

import com.github.mavenpractice.domain.entity.User;
import lombok.Data;

@Data
public class OrderUserDTO {
    private Long orderId;
    private String orderNo;
    private String orderStatus;
    private Long userId;
    private String username;
    private String email;

    public static OrderUserDTO from(User user) {
        OrderUserDTO dto = new OrderUserDTO();
        dto.setUserId(user.getId());
        dto.setUsername(user.getUsername());
        dto.setEmail(user.getEmail());
        return dto;
    }
}
