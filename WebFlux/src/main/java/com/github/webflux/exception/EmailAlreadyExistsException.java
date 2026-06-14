package com.github.webflux.exception;

public class EmailAlreadyExistsException extends RuntimeException {

    public EmailAlreadyExistsException(String email) {
        super("邮箱已存在，email=" + email);
    }
}
