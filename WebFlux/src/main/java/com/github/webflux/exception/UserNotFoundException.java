package com.github.webflux.exception;

public class UserNotFoundException extends RuntimeException {

    public UserNotFoundException(Long id) {
        super("用户不存在，id=" + id);
    }
}
