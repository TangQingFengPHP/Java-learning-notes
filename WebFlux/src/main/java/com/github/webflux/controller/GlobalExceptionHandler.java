package com.github.webflux.controller;

import com.github.webflux.exception.EmailAlreadyExistsException;
import com.github.webflux.exception.RemoteServiceException;
import com.github.webflux.exception.RemoteUserNotFoundException;
import com.github.webflux.exception.UserNotFoundException;
import com.github.webflux.model.ApiError;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.support.WebExchangeBindException;
import reactor.core.publisher.Mono;

import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(UserNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Mono<ApiError> handleUserNotFound(UserNotFoundException exception) {
        return Mono.just(ApiError.of(404, exception.getMessage()));
    }

    @ExceptionHandler(RemoteUserNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Mono<ApiError> handleRemoteUserNotFound(RemoteUserNotFoundException exception) {
        return Mono.just(ApiError.of(404, exception.getMessage()));
    }

    @ExceptionHandler(EmailAlreadyExistsException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public Mono<ApiError> handleEmailAlreadyExists(EmailAlreadyExistsException exception) {
        return Mono.just(ApiError.of(409, exception.getMessage()));
    }

    @ExceptionHandler(RemoteServiceException.class)
    @ResponseStatus(HttpStatus.BAD_GATEWAY)
    public Mono<ApiError> handleRemoteService(RemoteServiceException exception) {
        return Mono.just(ApiError.of(502, exception.getMessage()));
    }

    @ExceptionHandler(WebExchangeBindException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Mono<ApiError> handleValidation(WebExchangeBindException exception) {
        String message = exception.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(fieldError -> fieldError.getField() + " " + fieldError.getDefaultMessage())
                .collect(Collectors.joining("; "));
        return Mono.just(ApiError.of(400, message));
    }
}
