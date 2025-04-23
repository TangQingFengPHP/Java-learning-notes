package com.github;

/**
 * 泛型类
 * @param <T>
 */
public class Box<T> {
    private T content;

    public void set(T content) {
        this.content = content;
    }

    public T get() {
        return content;
    }
}
