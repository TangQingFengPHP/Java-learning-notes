package com.github.webflux.exception;

public class RemoteServiceException extends RuntimeException {

    public RemoteServiceException(String body) {
        super("远程服务异常：" + body);
    }
}
