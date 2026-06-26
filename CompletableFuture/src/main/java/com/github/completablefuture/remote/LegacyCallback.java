package com.github.completablefuture.remote;

public interface LegacyCallback<T> {

    void onSuccess(T result);

    void onError(Throwable throwable);
}
