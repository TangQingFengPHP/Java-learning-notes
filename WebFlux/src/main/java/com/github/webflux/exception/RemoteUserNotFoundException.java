package com.github.webflux.exception;

public class RemoteUserNotFoundException extends RuntimeException {

    public RemoteUserNotFoundException(Long id) {
        super("远程用户不存在，id=" + id);
    }
}
